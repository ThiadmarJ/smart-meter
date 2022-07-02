# smart-meter

## Purpose
The purpose of the 'smart-meter' api is to allow consumers to receive
information on their smart readings, through http requests.

## Authentication
username: 'user'

password: 'password'


## Usages
This api uses a **H2** database in order to store readings in memory
*(http://localhost:8081/h2)*

**MockMvc** is used for integration testing

**Basic Auth** is used for authentication with **BCrypt** to hash the stored password internally


## Endpoints
```
Request:
GET: http://localhost:8081/api/smart/reads/{ACCOUNTNUMBER}
Response:
{
 accountId: Number,
 gasReadings: [
    {
        id: Number,
        meterId: Number,
        reading: Number,
        date: Date
    }
 ],
 elecReadings: [
    {
        id: Number,
        meterId: Number,
        reading: Number,
        date: Date
    }
 ]
}
```