# S.C.H.I.E.T. Squash App
<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/main.png" align="right" height="326" width="183" >

The squash club 'S.C.H.I.E.T. Squash' wants to show all available trainings to their members. The best way to do this is with an app. Therefore, is now available the S.C.H.I.E.T. Squash App!

The mainscreen of the app shows all available trainings. These are always visible, so even when you are not signed in. But when you want to register you will have to login first. If you don't have an account don't worry because you can create one in the app. If you suddenly can't make it to the training or you just pressed a wrong training, you can easily cancel your registration.

Logged in or not, you will always have permission to go the the contact page. Here you can find some contact information about the club.

When users are logged in they will receive access to the MegaChat. Here all registered user of the app can speak with each other on one big message board. The app is also very dynamic. If you want to change your email address, password, user name you can easily do this at the user account page.

For the trainers there is a special page, the admin page. Here they can add new trainings, change them or even delete them. In the admin page all the previous (not deleted) trainings will remain visible.

### Design
Below you can see a clearly described flow of the app.

<img src="https://github.com/stephankok/progproject/blob/master/doc/final%20images/flow_chart.png" align="left" >

Activities
- MainActivity: Showing trainings to user, registration, deregistration
- LoginActivity: Logging on user, Registration of new users, send forgot password mail
- AdminActivity: Editing trainings, delete trainings, add new trainings
- ContactActivity: Show contact information about S.C.H.I.E.T. Squash
- UserAccountActivity: Change user details, log out
- SplashActivity: Show logo on startup of app (instead of white screen)
- MegaChatActivity: Mega chat for all users to use

Adapters
- MegaChatAdapter: Show messages
- UserTrainingAdapter: Show upcoming trainings to users
- EditTrainingAdapter: Show all trainings to admins
- FragmentInlogAdapter: Display Fragments on UI

Fragments
- ForgotPasswordFragment: Send mail to users email to change password
- LoginFragment: Login for users
- RegisterFragment: Register new users

Helpers
- CalendarPicker: 'Create a new training with start date, start time and end time', Change date, change start time, change end time
- FirebaseConnector: Get all trainings, update registered players, update all trainings, sort trainings list (On start time and date), rename user, delete user 

Modules
- Training:
  - date: Starting time and date
  - end: Ending time
  - trainer: Trainer
  - shortInfo: Title of training
  - subjectOfTraining: What are we gonna do on the training
  - currentPlayers: Amount of registered players
  - maxPlayers: Maximum amount of registered players
  - registeredPlayers: All players that are registered (unique user id, registered name)
  
- MegaChatMessage
  - userName: Name of user that send message
  - message: Message of user
  - timeStamp: Time that message is sent in milliseconds

Api
- https://firebase.google.com/
  - Real time database
  - Authentication
  - Crash reporting

#### Why i have created the app as it is
MainActivity and AdminActivity both use the Training model. But the property's of the adapters are completely different so i have created different adapters. One for user registration and one for editing.

Apps run smoother when a single subject can be performed within one activity and thus exist UserActivity of three fragments, one for logging in, one for registration and one if the user has forgotten his password. Registration for a training-, changing account details-, chatting- and editing trainings- Activities can all individually be created within one activity and thus don't need any fragments.

I created a class FirebaseConnector to get, update or change information on the database. Since the root of Firebase is already a singleton, this class doesn't have to be a singleton. A reference to an TextView is given to display errors. Firebase will validate all the input given by the users. Since connecting to Firebase takes time, some validation is done on the UI of the app.

Firebase only supports a few amounts of data formats. Namely, Strings, Longs, List<Object&gt;, Map<Object,Object&gt; so these data formats are the only types I used within my models.

Because each players has to be able to subscribe to trainings, i have implemented a login and registration option. Since everyplayer has its own phone they will stay logged on when they will close the app, unless they log out first.

### Challenges
The club has its own server. I wanted to create my own api, so my own communication between the app and the server. Unfortunately this didn't work and would take too much time to fix. So instead I used Firebase. Firebase works fine, but it had its own troubles. Firebase is recently taken over by Google, they changed the syntax of how you must implement Firebase. Because of this the code you find only mostly don't match with the current version of Firebase. I have been stuck with it for some time, but I got it to work.

Connecting to the Firebase will happen on the background. Communicating back to the UI has been a struggle, but with the use of an AsyncResonse it works fine.
