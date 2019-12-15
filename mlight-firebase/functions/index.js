const functions = require('firebase-functions');
const admin = require('firebase-admin')
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
admin.initializeApp();


exports.lightCommand = functions.https.onRequest(async(req, res) => {
    const cmd = req.query.cmd;
    // const snapshot = await admin.database.ref('mlight/command')
    // snapshot.ref.set(cmd)
    console.log(cmd)
    var db = admin.database()
    var ref = db.ref('/mlight');
    ref.set({
        "command": cmd
    })

    res.send('valid')
    return
})

exports.getLightCommand = functions.https.onRequest(async(req, res) => {
    var db = admin.database()
    var ref = db.ref('/mlight/command')
    ref.on("value", (snapshot) => {
        console.log(snapshot.val());
        res.send(snapshot.val())
        return
    })
})