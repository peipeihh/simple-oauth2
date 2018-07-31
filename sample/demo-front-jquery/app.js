let express = require('express');
let app = express();
app.use(express.static('src', {'index': 'index.html'}));

// app.get('/api/hello', function (req, res) {
//     res.send('Hello World!');
// });

app.listen(8600);
console.log("server is started, please open following url in the browser: ");
console.log("http://localhost:8600");