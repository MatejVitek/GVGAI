data <- read.csv("readable_data.csv")
data.in <- data.frame(data[1:8])
data.out <- data.frame(data[9:18])

df <- data.frame(data[1:8], data[9:18])
train <- df[1:floor(0.7*nrow(df)),]
test <- df[(floor(0.7*nrow(df))+1):nrow(df),]

#
# Algorithms
#

CA <- function(observed, predicted) {
	t <- table(observed, predicted)
	sum(diag(t)) / sum(t)
}
BS <- function (observedMatrix, predictedMatrix) {
	sum((observedMatrix - predictedMatrix) ^ 2) / nrow(predictedMatrix)
}

# nnet
library(nnet)

model.nn <- nnet(data.in, data.out, data = df, size = 12, decay = 0.0001, maxit = 10000)
pred.nn <- predict(model.nn, df[1:10,], type = "raw")

for (i in 1:10) {
	print(which.max(pred.nn[i,]) == i)
	print(max(pred.nn[i,]))
}

obsMat <- data.out[1:10,]
BS(obsMat, pred.nn)







# libsvm
library(e1071)
model.svm <- svm(Winner ~ ., data = train, cost = 1, gamma = 1/ncol(train), probability = T)
pred.svm <- predict(model.svm, test[,-1], probability = T)
predMat <- attr(pred.svm, "probabilities")
obsMat <- model.matrix(~Winner-1, test)

colnames(predMat)
colnames(obsMat)

BS(obsMat, predMat[,c(2,1,3)])
ca.svm <- CA(test$Winner, pred.svm)
ca.svm



# svmlight
library(klaR)
model.svmlight <- svmlight(Winner ~ ., data = train)
pred.svmlight <- predict (model.svmlight, test[,-1])

BS(obsMat, pred.svmlight$posterior)
ca.svmlight <- CA(test$Winner, pred.svmlight$class)
ca.svmlight



# pegasos
library(RSofia)
df2 <- df
df2$Winner <- as.character(df2$Winner)
for (i in 1:nrow(df2)) {
	if (df2[i,]$Winner == "A")
		df2[i,]$Winner <- 1
	else df2[i,]$Winner <- -1
}
df2$Winner <- as.numeric(df2$Winner)
train2 <- df2[1:floor(0.7*nrow(df2)),]
test2 <- df2[(floor(0.7*nrow(df2))+1):nrow(df2),]

model.sofia <- sofia(Winner ~ ., data = train2, trainer_type = "pegasos")
pred.sofia <- predict.sofia(model.sofia, test2, prediction_type = "linear")
predMat <- matrix(nrow = length(pred.sofia), ncol = 3)
predMat[,1] <- (1 + pred.sofia) / 2
predMat[,2] <- (1 - pred.sofia) / 2
predMat[,3] <- 0
obsMat2 <- matrix(nrow = length(pred.sofia), ncol = 3)
obsMat2[,1] <- (1 + test2$Winner) / 2
obsMat2[,2] <- (1 - test2$Winner) / 2
obsMat2[,3] <- 0

BS(obsMat2, predMat[,c(2,1,3)])
for (i in 1:length(pred.sofia)) {
	if (pred.sofia[i] > 0)
		pred.sofia[i] <- 1
	else
		pred.sofia[i] <- -1
}
CA(test$Winner, pred.sofia)

# kernel SVM
library(kernlab)
model.ksvm <- ksvm(Winner ~ ., data = train, kernel = "rbfdot", C = 1, prob.model = T)
pred.ksvm <- predict (model.ksvm, test[,-1], type = "prob")

BS(obsMat, pred.ksvm)
predVec <- vector()
for (i in 1:nrow(pred.ksvm))
	predVec[i] <- which.max(pred.ksvm[i,])
predVec <- as.factor(colnames(pred.ksvm)[predVec])
ca.ksvm <- CA(test$Winner, predVec)
ca.ksvm

# Least-squares SVM
# http://ap.isr.uc.pt/archive/paper_datam2_a2.pdf
model.lssvm <- lssvm(Winner ~ ., data = train, kernel = "rbfdot")
pred.lssvm <- predict (model.lssvm, test[,-1])
ca.lssvm <- CA(test$Winner, pred.lssvm)
ca.lssvm





#
# let's combine the successful algorithms
#

# let's try (weighted) voting
# weights given by the CAs of algorithms in above testing
# libsvm, svmlight, ksvm, lssvm

pred.svm <- attr(pred.svm, "probabilities")
pred.svm <- pred.svm[,c(2,1,3)]
pred.svmlight <- pred.svmlight$posterior
pred.lssvm.mat <- matrix(nrow = length(pred.lssvm), ncol = 3)
for (i in 1:length(pred.lssvm)) {
	if (toString(pred.lssvm[i]) == "A")
		pred.lssvm.mat[i,] <- c(1,0,0)
	else if (toString(pred.lssvm[i]) == "B")
		pred.lssvm.mat[i,] <- c(0,1,0)
	else
		pred.lssvm.mat[i,] <- c(0,0,1)
}

# lssvm only allows class return values, so adding it will result in skewed probabilities of only 1s and 0s.
# So we first try without it and then include it in the voting.

pred <- ca.svm * pred.svm + ca.svmlight * pred.svmlight + ca.ksvm * pred.ksvm
pred <- as.factor(levels(test$Winner)[apply(pred, 1, which.max)])
CA(test$Winner, pred)

pred <- ca.svm * pred.svm + ca.svmlight * pred.svmlight + ca.ksvm * pred.ksvm + ca.lssvm * pred.lssvm.mat
pred <- as.factor(levels(test$Winner)[apply(pred, 1, which.max)])
CA(test$Winner, pred)





#
# let's compare to the ANN and RF methods
#


# rf
library(randomForest)

model.rf <- randomForest(Winner ~ ., data = train)
pred.rf <- predict(model.rf, test, type = "prob")

BS(obsMat, pred.rf)
predVec <- vector()
for (i in 1:nrow(pred.rf))
	predVec[i] <- which.max(pred.rf[i,])
predVec <- as.factor(colnames(pred.rf)[predVec])
ca.rf <- CA(test$Winner, predVec)
ca.rf





#
# finally, add these two into the mix as well (leave out lssvm, as we saw it can negatively impact results)
#

pred <- ca.svm * pred.svm + ca.svmlight * pred.svmlight + ca.ksvm * pred.ksvm + ca.nn * pred.nn + ca.rf * pred.rf
pred <- as.factor(levels(test$Winner)[apply(pred, 1, which.max)])
CA(test$Winner, pred)

# try with only the best scoring SVM (kernel SVM)

pred <- ca.ksvm * pred.ksvm + ca.nn * pred.nn + ca.rf * pred.rf
pred <- as.factor(levels(test$Winner)[apply(pred, 1, which.max)])
CA(test$Winner, pred)





#
# Statistical tests to assess the probability that these differences are coincidental
#
source("statisticaltests.R")

predVec.svm <- as.factor(levels(test$Winner)[apply(pred.svm, 1, which.max)])
predVec.svmlight <- as.factor(levels(test$Winner)[apply(pred.svmlight, 1, which.max)])
predVec.ksvm <- as.factor(levels(test$Winner)[apply(pred.ksvm, 1, which.max)])
predVec.rf <- as.factor(levels(test$Winner)[apply(pred.rf, 1, which.max)])
predVec.nn <- as.factor(levels(test$Winner)[apply(pred.nn, 1, which.max)])
predVec.comb <- pred

preds <- data.frame(predVec.svm, predVec.svmlight, predVec.ksvm, pred.lssvm, predVec.rf, predVec.nn, predVec.comb)
obs <- test$Winner

# since draws are extremely rare, we can just replace them with "A" or "B" randomly, without 
# compromising our statistical test accuracy (so we can use tests for binary classifiers)
replace.draws <- function(column) {
	for (i in 1:length(column))
		if (toString(column[i]) == "D")
			column[i] <- levels(test$Winner)[sample(1:2, 1)]
	column
}
obs <- replace.draws(obs)
obs <- droplevels(obs)
for (i in ncol(preds))
	preds[,i] <- replace.draws(preds[,i])
preds <- droplevels(preds)
preds <- prepare.binary.treatments(preds, obs)

compare.cochranQ(preds)

# high p-value so we cannot conclude these models were all different
# let's compare the best-scoring SVM (ksvm) and the final model (combination of ksvm, nn, rf)
compare.mcnemar(preds[c(3,7)])

# we also get high p-value so we cannot reliably differentiate between the two
# let's compare the probability predictions of the two
pred <- (ca.ksvm * pred.ksvm + ca.nn * pred.nn + ca.rf * pred.rf)[,c(1,2)]
for (i in 1:nrow(pred))
	pred[i,] <- pred[i,] / sum(pred[i,])
pred.ksvm <- pred.ksvm[,c(1,2)]
for (i in 1:nrow(pred.ksvm))
	pred.ksvm[i,] <- pred.ksvm[i,] / sum (pred.ksvm[i,])

obsMat <- model.matrix(~obs)
se.ksvm <- apply((obsMat - pred.ksvm) ^ 2, 1, sum)
se.comb <- apply((obsMat - pred) ^ 2, 1, sum)
compare.wilcoxon.signrank(data.frame(se.ksvm, se.comb))

# can we at least say the final model is better than a simpler approach, such as a NB classifier?
# (nb is not appropriate since attributes are not independent)
model.nb <- naiveBayes(Winner ~ ., train)
pred.nb <- predict(model.nb, test, type = "class")
pred.nb <- droplevels(replace.draws(pred.nb))
CA(test$Winner, pred.nb)

preds <- data.frame(pred.nb, predVec.comb)
preds <- prepare.binary.treatments(preds, obs)
compare.mcnemar(preds)

# we get a low p-value (<0.03) this time, so at least our model is (almost) certainly better than an inappropriate approach - NB