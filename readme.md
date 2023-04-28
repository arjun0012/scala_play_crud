# scala play rest api

## routes 
### requests by employee with empId uid
GET /:uid/emp =>will show all employees of same company as employee with uid
GET /:uid/emp/:id =>dislay employee with empId id, will only be visible if company is same
POST /emp/add => add new employee
PUT /:uid/emp/update/:id =>update employee details of employee with empId id, can only be done if company is same
DELETE /:uid/emp/:id =>delete employee with empId uid,can only be done if company is same