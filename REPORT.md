# S.C.H.I.E.T. Squash App
<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/main.png" align="right" height="326" width="183" >

The squash club 'S.C.H.I.E.T. Squash' wants to show all available trainings to their members. The best way to do this is with an app. Therefore is now available the S.C.H.I.E.T. Squash App!

The mainscreen of the app shows all available trainings. These are always visable, so even when you are not signed in. But when you want to register you will have to login first. If you dont have an account don't worry because you can create one in the app. If you suddenly can't make it to the training or you just pressed a wrong training, you can easily cancel your registration.

Logged in or not, you will always have premission to go the the contact page. Here you can find some contact information about the club.

When users are logged in they will recieve acces to the MegaChat. Here all registered user of the app can speak with each other on one big messageboard. The app is also very dynamic. If you want to change your email adress, password, username you can easaly do this at the user account page.

For the trainers there is a special page, the admin page. Here they can add new trainings, change them or even delete them. In the admin page all the previous (not deleted) trainings will remain visable.

### Design
Below you can see a clearly described flow of the app.

<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/flow_chart.png" align="left" >

Activities
- MainActivity
- LoginActivity
- AdminActivity
- ContactActivity
- UserAccountActivity
- SplashActivity
- MegaChatActivity

Adapters
- MegaChatAdapter
- UserTrainingAdapter
- EditTrainingAdapter
- FragmentInlogAdapter

Fragments
- ForgotPasswordFragment
- LoginFragment
- RegisterFragment

Helpers
- CalendarPicker
- FirebaseConnector

Modules
- Training
- MegaChatMessage

#### Why i have created the app as it is
MainActivity and AdminActivity both use the Training model. But the property's of the adapters are completly diffrent so i have create diffrent adapters. One for user registration and one for editting.

Apps run smoother when an single subject can be performed within one activity and thus exist UserActivity of three fragments, one for loggin in, one for registration and one if the user has forgotten his password. Registration, changing account details, chatting and editting Activities can all individually be created within one activity and  thus don't need any fragments.

I created a class FirebaseConnector to get, update or change information on the database. Since the root of firebase is already a singleton, this class doesn't have to be a singleton. A refference to an TextView is given to display errors. Firebase will validate all the input given by the users. Since conencecting to firebase takes time, some validation is done on the app.

Firebase only supports a few amounts of dataformats. Namely: Strings, Longs, List<Object>, Map<Object,Object>, so these dataformats are the only types i used within my models.

Because each players has to be able to subscribe to trainings, i have implemented an login and registration option. Since everyplayer has its own phone they will stay logged on when they will close the app, unless they log out first.

### challenges
The club has its own server. I wanted to create my own api, so my own communication between the app and the server. Unfortinatly this didn't work and would take to much time to fix. So instead i used Firebase. Firebase works fine, but it had its own troubles. Firebase is recently taken over by google, they changed the sintax of how you must implement firebase. Because of this the code you find only mostly dont match with the current version of firebase. I have been stuck with it for some time, but i got it to work.
