
### Purpose

The aim is to build a <i>live-transcription broadcasting service</i> for use in settings 
of public gathering. In such settings, the speech recorded from the speaker is transcribed 
and broadcasted to members of the audience in nearly real time. People with special hearing 
or language needs may benefit from the visual aid provided by this service in such environments 
as lectures and conferences.

Incorporating this live-transcription broadcasting service with the Google Glass device provides
the user with an immersive experience with minimal distraction. It allows the user to visually
focus on the speaker while having supplementary transcriptions available within view.

### Context

The <i>live-transcription broadcasting service</i> has three components: the <b>Server</b>, the
<b>Clients</b> and the <b>Backbone</b>.

- The <b>Backbone</b> is the Watson Speech-To-Text (STT) service which is responsible for performing 
the live transcription.

- The <b>Server</b> resides on Bluemix. It acts as the mediator between the clients and the STT service. 
Audio received from the client (in the recorder role) is passed on to the STT service. When 
transcription result is available from the STT service, the server broadcasts the result to all 
clients.

- The <b>Clients</b> may come from the web (web client), a mobile device (mobile client), or the Google 
Glass (Google Glass client).
There are two roles that a client can play: to be a recorder or a receiver. A recorder may submit audio
recording for transcription. Receivers only receive the transcription results. The Google Glass client
currently only plays the role of a receiver.

This project builds the <b><i>Google Glass client</i></b> component.


### Design of the Google Glass Client

The Google Glass client is equipped with both the wifi and the bluetooth services. The user connects
to the server via wifi to receive live transcriptions. The transcriptions may be displayed directly
on the glass itself or be projected onto a bluetooth-paired mobile device.

The client connects to the server through socket.io.


### Source code

- Server: https://hub.jazz.net/project/wrstar/SpeechToText-broadcast/overview
- Android client: https://github.com/RStarTec/SpeechToTextBroadcaster-Android
- Google Glass client: https://github.com/RStarTec/SpeechToTextReceiver-GoogleGlass

### TODO

1. The Google Glass client may also be set up as a recorder.

### Terms of Use

This project is under the Apache2 license. 
The author(s) intended this project for beneficial uses in society and would appreciate this 
wish to be respected.

### Contact

Please contact us for any feedback or suggestions. Thank you!
