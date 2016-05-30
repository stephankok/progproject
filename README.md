Programmeer Project
==============
Schiet Squash
--------------
*Door Stephan Kok van de Universiteit van Amsterdam.*

De squash club (www.schietsquash.nl) wilt een app voor de leden. Hier willen ze trainingen aanbieden waar leden zich voor kunnen inschrijven. Er moet een duidelijk weergave van de verschillende trainingen komen en wanneer ze zijn. Het is ook belangrijk dat mensen zich uit kunnen schrijven als ze toch niet kunnen komen. Idealiter moeten de beheerders van de app (De voorzitters van de club) de trainingen kunnen aanpassen, updaten en verwijderen via de app.

Voor deze app zal een eigen api gemaakt moeten worden. Deze moet get request en put request kunnen hendelen. Hiervoor is tot nu toe (www.myjson.com) gevonden. Maar het zal iedealer zijn als dit via hun eigen server (wordpress) mogelijk is. Dit zal vrij veel werk zijn en zal ik me eerst in verdiepen.

Dit ga ik maken door een aantal activity's, met arrayadapters te gebruiken en zoals eerder vermeld een eigen api te maken. Zie ook wel onder voor meer details.

Als extra's zou de ladder competitie ook in de komen, waarbij de stand door zou gegeven kunnen worden via de app (ipv via de mail) en dit dan live geupdate kan worden. Een andere optie is een Mega chat waar iedereen op kan reageren. Hier zal wel een vorm van inlog/database voor nodig zijn. Het inloggen zal ook handig zijn om gebruikers te laten registeren. Voor het mogelijk maken van inloggen zal echter wel een database gemaakt moeten worden en aangezien de club ook geen inlog voor de website heeft is dit mischien niet ideaal.

Activity’s
-	startscherm: korte welkom en laat de beschikbare trainingen zien waar gebruikers mensen kunnen inloggen.
-	Training admin edit: activity is beveiligd met inlog, laat admins trainingen wijzigen
-	Contact activity: gevens van de club, hoe ze te benaderen zijn, mogelijk inschrijf formulier
-	Extra: laddar, mega chat, inlog

http/asynctask
-	GET, om gebruikers de trainingen te laten zien
-	PUT, om admins de trainingen te laten updaten

Adapters
-	Training view: Weergave en inschrijven van trainingen
-	Training edit: Weergave en aanpassen van trainingen.
-	Extra: ladder, mega chat, inlog

API 
-	Training: Jsonformat waar de trainings gevens staan, moet aanpasbaar zijn (www.myjson.com)
-	Extra: ladder met stand, megachat, inlog



Screenshots:



<img src="https://github.com/stephankok/progproject/blob/master/doc/start_activity_training.png" align="left" height="300" width="150" >
<img src="https://github.com/stephankok/progproject/blob/master/doc/login_admin.png" align="left" height="300" width="150" >
<img src="https://github.com/stephankok/progproject/blob/master/doc/admin_edit_training.png" align="left" height="300" width="150" >
<img src="https://github.com/stephankok/progproject/blob/master/doc/contact_information.png" align="left" height="300" width="150" >

<br></br>
<br></br>
<br></br>
<br></br>
<br></br>
<br></br>


**Bestaande apps**

Vooral sportscholen bieden soms vergelijkbare apps aan. Waar leden zich kunnen inschrijven voor trainingen. Zie ook wel https://play.google.com/store/apps/details?id=br.com.wiki4fit & https://play.google.com/store/apps/details?id=org.lredacademy.training.

Aangezien deze app speciaal ontworpen moet worden per vereniging is het niet mogelijk een bestaande app te gebruiken. Wel zouden vergelijkbare ideeën mischien toepasbaar. Zo zie je in bovenstaande apps dat ze ook gebruik maken van een inlog systeem en een chat systeem.

Copyright (c) 2016 Stephan Kok
