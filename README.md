Programmeer Project
==============
Schiet Squash
--------------
*Door Stephan Kok van de Universiteit van Amsterdam.*

De squash club (SchietSquash) wilt een app voor de leden. Hier moeten willen ze trainingen aanbieden waar leden zich voor kunnen inschrijven. Er moet een duidelijk weergave van de verschillende trainingen komen en wanneer ze zijn. Het is ook belangrijk dat mensen zich uit kunnen schrijven als ze toch niet kunnen komen. Idealiter moeten de beheerders van de app (De voorzitters van de club) de trainingen kunnen aanpassen, updaten en verwijderen via de app.

Voor deze app zal een eigen api gemaakt moeten worden. Deze moet get request en put request kunnen hendelen. Hiervoor is tot nu toe (www.myjson.com) gevonden. Maar het zal iedealer zijn als dit via hun eigen server (wordpress) mogelijk is.

Activity’s
-	Begin: introductie, verwijzen naar functies in de app
-	Traingingen activity: laat de beschikbare trainingen zien waar gebruikers mensen kunnen inloggen.
-	Training admin edit: beveiligd met inlog, laat admins trainingen wijzigen
-	Contact activity: gevens van de club, hoe ze te benaren zijn, mogelijk inschrijf formulier
-	Extra mogelijkheden: laddar, mega chat

http/asynctask
-	GET, om gebruikers de trainingen te laten zien
-	PUT, om admins de trainingen te laten updaten

Adapters
-	Training view: Weergave en inschrijven van trainingen
-	Training edit: Weergave en aanpassen van trainingen.
-	extra: ladder, mega chat

API 
-	Training: Jsonformat waar de trainings gevens staan, moet aanpasbaar zijn (www.myjson.com)
-	Extra: ladder met stand, megachat


Copyright (c) 2016 Stephan Kok
