# Analyse

## Mindmap


## Beschrijving

Herexamen Smart Systems 2017-2018 aan de AP Hogeschool voor de richting Elektronica-ICT.  
Gemaakt door Carl Vancluysen, 2EA2.

Doel van het project is een opstelling maken met een servo motor waarop twee ultrasone sensoren op gemonteerd staan.  
De opstelling is in staat zijn plaats te bepalen in een bak.

De sensoren staan in een hoek van 90° op de servo motor. Opstelling kan op gelijke welke hoek in de bak geplaatst worden,
de opstelling gaat dan automatisch de ultrasone sensoren loodrecht uitlijnen tegenover de wanden van de bak.

Via bluetooth kan je een coördinatenpaar doorsturen naar de opstelling.  
De opstelling gaat daarna zijn huidig coördinatenpaar vergelijken met de gewenste locatie.   
Als er een match is dan gaat er een LED branden.

## Hardware analyse

## Software analyse

## Task list

1.  Opbouwen breadboard / Connecteren
2.  Android Applicatie
3.  Bluetooth
4.  Afstand Sensoren
5.  9DOF stick
6.  Servo motor
7.  Volledige functionaliteit
8.  PCB
9.  Documentatie / Presentatie
10. Kleine aanpassingen / Bug Fixes

## Systeemspecificaties

Arduino UNO  
2x Afstandssensoren  
Servo Motor  
HC-06 Wireless Bluetooth Module  
SparkFun LSM9DS1 9DOF Stick  
Android Device  
2x 330Ω weerstanden  
1x Rood LED  
1x Groen LED  
2x Kleine breadboarden  
Platform voor de servomotor  
Bak gemaakt uit karton  
Powerbank  
In elkaar gehouden door combinatie van secondelijm en plakbank

## Toegepaste Oplossing

Twee sensoren op kleinere breadboards die gemonteerd zijn op de servo motor.  
Via een Bluetooth Socket verbinding te maken door gebruik te maken van een Android Smartphone kunnen we een connectie verkrijgen met de opstelling.  
Opstelling plaatsen zodat de sensoren loodrecht uitlijnen met de bak, daarna zullen we onze 9DOF stick kalibreren door een knop op de smartphone.  
//TODO kalibratie  
Na dit gebeurd is kunnen we de opstelling plaatsen in de bak en zal de servo motor reageren op de gyroscoop van de 9DOF stick.  
Als we tussen 0-180 zitten zullen de sensoren gericht staan op de muren waar we ze eerst op hebben gericht.  
Tussen 180-360 zal de servo motor draaien zodat de sensoren zich richten op de andere muren.  
Via onze smartphone geven we de afmeting mee van de bak, zodat het resultaat dat gemeten is meer accuraat is.  
Daarna kunnen we een X en Y positie meegeven met een Guess knop.   
Als de gewenste locatie overeenkomt (10cm error margin) met het huidig coördinatenpaar zal de groene LED aangaan.  
Als dit niet het geval is, kan je opnieuw blijven proberen.  
Als dit wel het geval zou zijn en de groene LED aangaat, kan je dit resetten door gebruik te maken van de reset knop en opnieuw proberen/spelen.

## Andere mogelijke oplossingen 
In plaats van afmetingen van de bak mee te geven zouden we ook de sensoren de afstand kunnen laten meten, maar dit is minder accuraat.  
## PCB

## Conclusie
