# S.C.H.I.E.T. Squash App
<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/main.png" align="right" height="326" width="183" >

The squash club 'S.C.H.I.E.T. Squash' wants to show all available trainings to their members. The best way to do this is with an app. Therefore is now available the S.C.H.I.E.T. Squash App!

The mainscreen of the app shows all available trainings. These are always visable, so even when you are not signed in. But when you want to register you will have to login first. If you dont have an account don't worry because you can create one in the app. If you suddenly can't make it to the training or you just pressed a wrong training, you can easily cancel your registration.

Logged in or not, you will always have premission to go the the contact page. Here you can find some contact information about the club.

When users are logged in they will recieve acces to the MegaChat. Here all registered user of the app can speak with each other on one big messageboard. The app is also very dynamic. If you want to change your email adress, password, username you can easaly do this at the user account page.

For the trainers there is a special page, the admin page. Here they can add new trainings, change them or even delete them. In the admin page all the previous (not deleted) trainings will remain visable.

Design
--------------
<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/flow_chart.png" align="left" >
A clearly described flow of the app.

#### Why i have created the app as it is
There are three main Activities. Main activity where trainings are shown and users can registrate, Admin activity where admins can edit trainings and Inlog activity where users can login.

I created a class FirebaseConnector to get, update or change information on the database. Since the root of firebase is already a singleton, this class doesn't have to be a singleton. A refference to an TextView is given to display errors. Firebase will validate all the input given by the users. Since conencecting to firebase takes time, some validation is done on the app.

#### Activities
- MainActivity
- LoginActivity
- AdminActivity
- ContactActivity
- UserAccountActivity
- SplashActivity
- MegaChatActivity

#### Adapters
- MegaChatAdapter
- UserTrainingAdapter
- EditTrainingAdapter
- FragmentInlogAdapter

#### Fragments
- ForgotPasswordFragment
- LoginFragment
- RegisterFragment

#### Helpers
- CalendarPicker
- FirebaseConnector

#### Modules
- Training
- MegaChatMessage

### challenges
De club heeft zijn eigen server, ik wou een eigen api maken om te communiceren tussen de server en de app. Dit is helaas niet gelukt omdat dit te veel tijd zou kosten. In plaats hiervan heb ik firebase gebruikt. Firebase had ook zijn eigen valkuilen, zo bleek het net overgenomen te zijn door google. Dit zorgde ervoor dat heel veel sintax veranderd was en niet meer klopte met wat online te vinden was.

Alle informatie in de juiste format krijgen was ook veel werk. Zo kan firebase maar met een beperkt aantal types van data werken en was ik gelimiteerd tot String, Long, List<Object> en Map<Object,Object>. Dit zorgde ervoor dat ik mijn app moest bouwen om dit dataformat inplaats van de dataformat om mijn app.

Elke speler van de club moet zich kunnen registreren. Hiervoor heb ik uiteindelijk besloten om een inlog te maken, anders zouden medespelers elkaar kunnen in en uitschrijven.
