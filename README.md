# GVGAI
General Video Game AI Competition

V src/org.neuroph je knjižnica Neuroph.

V datasets so učne in testne množice, ter nejc.csv, ki vsebuje Nejčevo ročno klasifikacijo. Zraven je še R skripta.
V nn in rf so datoteke, v katerih so shranjeni klasifikatorji.

V src/matej je vsa moja koda.
V paketu matej je razred Agent, ki vsebuje nekakšen template, kako bi končni agent, ki bi uporabljal moje klasifikatorje izgledal.

V paketu matej.nn je moja koda za nevronsko mrežo:
  - NNHandler je razred, ki ga agent pokliče, da dobi napoved od nevronske mreže. Poskrbi za branje nevronske mreže iz datoteke (ki mora biti narejena vnaprej).
  - NNCreator je razred, ki ima metode za izdelanje učne in testne množice, učenje nevronske mreže in shranjevanje le-te v datoteko, in testiranje nevronske mreže na testni množici.
  - DataSetAgent je razred, s katerim si NNCreator pomaga pri izdelavi učne in testne množice.
  
V paketu matej.rf je moja koda za naključni gozd:
  - RFHandler je razred, ki združuje Handler in Creator podobno kot zgoraj.
  - DataSetAgent je spet samo pomožni razred.
  - Preostali razredi so moja implementacija naključnega gozda.
  
Ostalo je iz gvgai frameworka.
