fetch('http://localhost:8080/api/content')
.then(Response => Response.json())
.then(data => console.log(data))