# KiReviewClassifier
Projekt zur Erkennung einer positvien oder negativen FilmReview. <br>
!!! Weka.jar muss als Library inkludiert sein. !!!

## PreProcessing: 
preDataprocessing erstellt eine neue Datei (Name alter Datei, mit Endung ...<Processed.arff>.
<br>Den Namen der Datei gibt man als Feld der Klasse an.

Dabei wird jede Review eingelesen, verarbeitet und in die neue Datei geschrieben.

Zur Verarbeitung werden zwei Listen genutzt, welche eine Menge englischer Wörter beinhalten. <br>
Diese Wörter sind entweder positiv oder negativ konnotiert. <br>
Wenn ein Wort einer Review nicht in einer der Listen enthalten ist, so wird es entfernt. <br>
Aus der Review: <br>

>'In the world of shorts (most of which arent) this film is a gem.A quiet concise peek into the world of a young woman whos a reader for a blind woman here the stellar Elizabeth Franz - this film bears the textures layers and visual storytelling of a sumptuously painted still life.The dialogue is minimal the cinematography is stunning and the direction sure clear and compelling. I saw this film in a film festival held in a loud and crowded Tribeca bar - and within the first two minutes (and for the first time that night) the crowd fell quiet.That says it all.'

wird: <br>

>'quiet concise blind stellar sumptuously stunning clear loud crowded fell'

Die Listen sind von GitHub: <br> <br>
positiv: https://gist.github.com/mkulakowski2/4289437 <br>
negativ: https://gist.github.com/mkulakowski2/4289441
<br>Es wurden aber noch von Hand ein paar Worte hinzugefügt.<br>

Außerdem wird "not" besonders behandelt.

besonders behandelt: <br>
not [...] <positives/negatives Wort> -> not<positives/negatives Wort> <br>
Bsp.: 'not so good' -> 'notgood'

## Classification

### class DevelopTestAndSaveModel:<br>
Mit dieser Klasse kann man seinen eigen Decision Tree auf Basis des J48 erstellen lassen. <br>

Dafür gibt man 4 Parameter in der Instanziierung von "classifier" an: <br>
__numToSelect...__ Anzahl der Attribute, welche vom AttributSelection Filter beibehalten werden.<br>
__wordsToKeep...__ Anzahl der Attribute, welche vom StringToWordVector Filter beibehalten werden. <br>
__trainingsSet...__ Name der zu grunde liegenden Trainings Daten Datei. <br>
__testSet...__ Name der zu grunde liegenden Test Daten Datei. <br>
__isJ48...__ Ob ein J48 Klassifikationsalgorithmus benutz werden soll, wenn false: RandomForest<br>
<br>
Es wird ein Decision Tree gebaut und ausgegeben. <br>
Wenn man diesen speichern will, kann man ihm einen Namen geben und er wird gespeichert unter __Models/Classifier__ und __Models/StringToWordVector__, oder man drückt Enter und der Tree wird verworfen. <br>
Wenn man den Baum speichert, wird zudem eine Infodatei erstellt, welche die Evaluation abspeichert.

### class LoadAndTestModel:
Mit dieser Klasse kann man einen zuvor generierten Tree laden und auf ein neues Testset anwenden.<br>
Man gibt dafür die Datei und den namen des Models an. Zudem auch ob es sich um ein NaiveBayes Model handelt.

### class getFalsePredictions:
Mit dieser Klasse kann man sich alle Reviews ausgeben lassen, diese werden in die Datei __FalsePredictions/falsePredictions.txt__ geschrieben

### class NaiveBayesDevelopTestAndSave
Mit dieser Klasse kann man sich einen NaiveBayes Classifier erstellen, welcher durch die eingegebenen Trainingsdaten trainiert und auf die eingegebenen Testdaten angewendet.
Den Classifier kann man dann wie bei __DevelopTestAndSaveModel__ speichern, das Model wird dann in __Models/Classifier/NaiveBayes__ gespeichert.
### class NaiveBayesLoadAndTest
Mit dieser Klasse kann man einen NaiveBayes Classifier einlesen und auf neue Testdaten anwenden.

### class GetFalsePredictionOfModel
Diese Klasse gibt alle falschen Voraussagen in "FalsePrediction/falsePrediction.txt" aus. 

<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>    




