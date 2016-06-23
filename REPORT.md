# S.C.H.I.E.T. Squash App

### Info
De squash club 'S.C.H.I.E.T. Squash' wilt graag een app waar ze trainingen kunnen aanbieden aan hun leden. Hiervoor is deze app gemaakt.
Het hoofdscherm zijn de trainingen. Deze zijn altijd zichtbaar, ook als je niet ingelogd bent. Mocht je je willen registreren voor de training moet je echter wel eerst inloggen. Als je ingelogd bent kun je ook nog bij de Mega Chat wat één groot gesprek is voor alle geregistreerde van de app. Mocht je foutief hebben ingeschreven hebben of kun je tog niet? Geen probleem want je kunt je gewoon uitschrijven.
Voor de trainers van de club is er een Admin page gemaakt. Hier kunnen ze trainingen toevoegen, wijzigen en verwijderen. De trainingen uit het verleden (die niet meer zichbaar zijn voor leden) zijn ook hier terug te vinden.
Mocht je andere informatie nodig hebben is er een contact pagina waar je informatie kunt vinden over de locatie van de club en de contact gegevens.

### Design
Clearly describe the technical design: how is the functionality implemented in your code? This should be like your DESIGN.md but updated to reflect the final application. First, give a high level overview, which helps us navigate and understand the total of your code (which components are there?). Second, go into detail, and describe the modules/classes and how they relate.

#### Activities
- Main
- Inlog
- Admin
- Contact
- Account
- Splash
- Mega Chat

#### Adapters
- Chat
- Users
- Admin
- FragmentInlog

#### Fragments
- Forgot password
- Login
- Register

#### Helpers
- Calendar Picker
- Firebase

#### Modules
- Training
- Message

Clearly describe challenges that your have met during development. Document all important changes that your have made with regard to your design document (from the PROCESS.md). Here, we can see how much you have learned in the past month.
Api, opslaan, verwerken

### challenges
De club heeft zijn eigen server, ik wou een eigen api maken om te communiceren tussen de server en de app. Dit is helaas niet gelukt omdat dit te veel tijd zou kosten. In plaats hiervan heb ik firebase gebruikt. Firebase had ook zijn eigen valkuilen, zo bleek het net overgenomen te zijn door google. Dit zorgde ervoor dat heel veel sintax veranderd was en niet meer klopte met wat online te vinden was.

Alle informatie in de juiste format krijgen was ook veel werk. Zo kan firebase maar met een beperkt aantal types van data werken en was ik gelimiteerd tot String, Long, List<Object> en Map<Object,Object>. Dit zorgde ervoor dat ik mijn app moest bouwen om dit dataformat inplaats van de dataformat om mijn app.

Elke speler van de club moet zich kunnen registreren. Hiervoor heb ik uiteindelijk besloten om een inlog te maken, anders zouden medespelers elkaar kunnen in en uitschrijven.
