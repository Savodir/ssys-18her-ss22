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
![Hardware Analyse](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/Hardware%20Analyse.png)  
Bluetooth verbinding tussen opstelling en Android device via een HC-06 op 5V.  
2 Ultrasone sensoren verbonden met de Arduino UNO op 5V.  
9DOF Stick verbonden met de Arduino UNO op 3.3V.  
Geeft de heading terug door gebruik te maken van de gyroscoop.  
Servo Motor verbonden met de Arduino UNO op 5V, past zijn eigen aan a.h.v.d. de gegeven heading.  
2 LEDs verbonden met de Arduino UNO, met weerstanden van 330 Ohm.  
## Software analyse
![Software Analyse](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/Software%20Analyse.png)  
Sensoren voor X en Y positie gebruiken de New Ping library om de juiste afstanden te bepalen tov de muur.  
Deze coördinaten worden elke loop vergeleken met de gewenste coördinaten om te zien of het correct is.  
Als dit correct is zal de groene LED aanspringen, is dit niet het geval blijft de rode LED branden tot het correct is.  
//TODO Kalibratie  
De servo motor zal zijn eigen constant aanpassen a.d.h.v. de heading van de 9DOF stick.  
Voor de Android applicatie hebben we eerst de optie knoppen.  
Device Selection: Openen van een dialog box met alle gepairde devices.  
Connect: Maken van een Bluetooth Socket verbinding met de opstelling.  
Calibratie: Kalibratie van de 9DOF stick starten.  
Dan hebben we Lengte en Breedte editText boxen, hiermee gaan we de grootte meegeven van de bak.  
Als laatste hebben we de X/Y waardes waarmee we de afstand kunnen raden.  
Data transmission: we translaten alles wat we moeten doorsturen naar bytes, deze gaan dan via de Bluetooth socket naar de opstelling.  
Daar worden de bytes aan elkaar geplakt.  
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
### Woord vooraf
- Geen PCB Design als keuzevak
- Geen toegang tot cursus
- Samenwerking met Elke Reynard
### Schema
![Schema](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/PCB%20schema.png)  
### PCB
Voeding van 5V aansluiten.  
Alle aansluitingen op externe componenten staan aan de kant van de PCB zoals het moet.  
Plaats voorzien om ATmega chip gemakkelijk in/uit het voetje te ha;en.  
UART aansluiten = Voeding loskoppelen. 
![PCB](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/Presentatie/PCB.png)  
### 3D
![3D Boven](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/Presentatie/PCB%20boven.png)  
![3D Onder](https://raw.githubusercontent.com/Savodir/ssys-18her-ss22/master/doc/img/Presentatie/PCB%20onder.png)  

### Problemen
1. Kristal moet uitgesneden worden op bepaalde PCB printers.  
2. Standaardbreedte van de baantjes verhoogd.  
3. Diameter van de pads moet groter gemaakt worden om goed te kunnen solderen.  
## Conclusie

### Ervaring

- Goed individueel project
- Leren solderen
- Meer ervaring met Arduino

### Wat kon beter

- Gebruik maken van registers
- Knutselwerk opstelling
- Solo PCB Design
- Code verbeteren
