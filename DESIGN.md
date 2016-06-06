Design
=====

**Classes**
- Training
  - Door wie
  - Wanneer
  - Hoelaat
  - Korte info
  - Aanmeldingen
  - Max aantal
- Http GET
  - get info van server
- Http PUT
  - update json op server
- Asynctask
  - Doe alle Http requests
_ TrainingArrayAdapter
  - Laat trainingen zien aan gebruikers
  - aanmeld mogelijkheid
  - afmeld mogelijkheid
  - extra informatie over training
- AdminTrainingArrayAdapter
  - Delete trainings
  - verander volgorde
  - voeg trainingen toe
  - verander trainings informatie
- Menu
  - Squash logo
  - navigatie door app

**API**
- mvp Firebase voor dataopslag
- firebase voor inlog, registratie
- database voor ladder

_Training informatie.  Ik heb mijn eigen api nodig. Deze ga ik proberen zelf te maken op de squash server. Als die niet gaat lukken ga ik dit doen via de gratis website www.myjson.com
   Geeft json terug met het aantal beschikbare trainingen
     Daarin voor elke training de aanwezige informatie._

**Database**
- mvp Firebase voor trainingen (in trie structuur)
- ledenlijst, ladder 
_ Op de api staan alle gegevens van de trainingen. Hier staan ook de mensen die geregistreerd zijn. Het is dus niet noodzakelijk iets lokaal op te slaan.
    
**Schets van app**

<img src="https://github.com/stephankok/progproject/blob/master/doc/design_schema.jpg" align="left" >
