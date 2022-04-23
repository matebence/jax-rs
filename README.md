## Jersey (JAX-RS)

**Web services**
- REST
	- HTTP protocol
	- Format JSON
	- Docs NONE
- SOAP
	- SOAP protocol
	- Format XML, TEXT
	- Docs WSDL

**HTTP methods**
- GET
	- Getting data
- POST
	- Creating data
- PUT
	- Updating data
- DELETE
	- Deleting data

**Metadata**
- Header
	- Authorization
	- Accept
	- Content type
- Cookie
- Status
	- 100 - Informational
	- 200 - Successful code
	- 300 - Redirection
	- 400 - Client error
	- 400 - Server error

**API design first identify the Resources (example)**
- Items Resource
	- List items
	- View item
	- Search for items
	- Add/edit items
- Carts Resource
	- Add item
	- Remove item
	- Clear cart
	- View cart
	- Checkout
- Orders resource
	- List orders for customer
	- Obtain order status
	- Cancel order
- Customers resource
	- Search for customers
	- View customer
	- Add/edit customer

**Mapping our Acitivities to (paths are plural not singular)**
- nouns
- verbs

**Caching**
In HTTP 1.1 the Cache-Control header specifies the resource caching behavior as well as the max age the resource can be cached
- `private` only clients (mostly the browser) and no one else in the chain (like a proxy) should cache this
- `public` any entity in the chain can cache this
- `no-cache` should not be cached anyway
- `no-store` can be cached but should not be stored on disk (most browsers will hold the resources in memory until they will be quit)
- `no-transform` the resource should not be modified (for example shrink image by proxy)
- `max-age` how long the resource is valid (measured in seconds)
- `s-maxage` same like max-age but this value is just for non clients

Conditional requests are those where the browser can ask the server if it has an updated copy of the resource. The browser will send one or both of the ETag and If-Modified-Since headers about the cached resource it holds. The server can then determine whether updated content should be returned or the browser’s copy is the most recent.

In JAX-RS the Request object has a method to check wether the data based on the date or the etag from the client was modified. The ETag value should be a unique hash of the resource and requires knowledge of internal business logic to construct. One possibility is to use the HashCode of a object as the etag

**REST constrains**
- Client-Server
	- Architecture must be CLIENT <-> SERVER
- Stateless
	Each request stand on its own
- Cacheable
	Each message should describe whether its cacheable
- Layered System
	- Logging
	- Authorization
- Uniform Interface
	- Only one way to get the resource - well formated URLs and metadata
- Hypermedia options
	- Useage of HATEOAS

**URL hierarchy**
- First level
	- /profiles/{id}
	- /profiles/{profileName}
- Second level (relations - one to many)
	- /messages{id}/comments/{id}
	- /messages{id}/likes/{id}

[The Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)
- Level 0
	- The starting point for the model is using HTTP as a transport system for remote interactions.
- Level 1
	- The first step towards the Glory of Rest in the RMM is to introduce resources. So now rather than making all our requests to a singular service endpoint, we now start talking to individual resources.
- Level 2
	- Useage of HTTP methods GET POST PUT DELETE
- Level 3
	- Using HATEOAS

> Most used status codes

|Code 			|Description 	   			   |
|---------------|------------------------------|
|200 			|OK 						   |
|201 			|Created 					   |
|204 			|No content 				   |
|302 			|Found  					   |
|307 			|Temporary redirect   		   |
|304 			|Not modfied 		 		   |
|400 			|Bad Request 				   |
|401 			|Unauthorized 				   |
|403 			|Forbidded 					   |
|404 			|Not Found 					   |
|415 			|Unsupported Media type Found  |
|500 			|Internal server error 		   |

> Status example

|Action 			|Endpoint 	   			   |Method 					|Status 			|Code 				|
|-------------------|--------------------------|------------------------|-------------------|-------------------|
|Get message 		|/messages/{id} 		   |GET 					|Success 			|200 				|
|Get message 		|/messages/{id} 		   |GET 					|Not found 			|404 				|
|Get message 		|/messages/{id} 		   |GET 					|Failure 			|500 				|
|Delete message     |/messages/{id} 		   |DELETE 					|Success  			|200 or 204 		|
|Delete message     |/messages/{id} 		   |DELETE 					|Not found 			|404 				|
|Delete message     |/messages/{id} 		   |DELETE 					|Failure 			|500 				|
|Edit message       |/messages/{id} 		   |POST 					|Success  			|200 				|
|Edit message       |/messages/{id} 		   |POST 					|Wrong format 		|400 or 415 		|
|Edit message       |/messages/{id} 		   |POST 					|Failure  			|500 				|
|Create message     |/messages 	 			   |PUT 					|Success  			|201 				|
|Create message     |/messages 	 			   |PUT 					|Wrong format		|400 or 415 		|
|Create message     |/messages 	 			   |PUT 					|Failure  			|500 				|

> Resource exmaple

|Method 	|URL					   |Description 	   			  			 	  |
|-----------|--------------------------|----------------------------------------------|
|GET 		|/messages 			   	   |Get all messages 						  	  |
|DELETE 	|/messages/9/comments	   |Delete all comments for message with id 9  	  |
|GET 		|/messages/5/comments      |Get all comments for message with id 5  	  |
|POST 		|/messages/7/comments  	   |Creates a new comment for message with id 7   | 
|PUT 		|/messages/20/comments     |Replaces all comments for message with id 20  | 

> Example of HATEOAS

```json
[
	{
		"id": "10",
		"message": "Hello world",
		"created": "2014-06-01T18:06:36.902",
		"author": "kosuhik"
		"links" [
			{
				 "href": "/messages/1",
				 "rel": "self"
			},
			{
				 "href": "/messages/1/comments",
				 "rel": "comments"
			}
		]
	}
]
```

> #### Wildfly setup

```bash
# Download Wildfly
wget https://download.jboss.org/wildfly/24.0.1.Final/wildfly-24.0.1.Final.zip

# Switch to Wildfly directory
cd WILDFLY_HOME

# Start server in standalone mode
standalone.bat

# Connect to management cli
jboss-cli.bat --connect

# Add a Simple Role Decoder which maps the application Roles from the attribute Roles in the File system.
/subsystem=elytron/simple-role-decoder=from-roles-attribute:add(attribute=Roles)

# Let’s define a new filesystem-realm named fsRealm and its respective path on the file system:
/subsystem=elytron/filesystem-realm=ApiRealm:add(path=demofs-realm-users,relative-to=jboss.server.config.dir)

# Next, we add some identities to the Realm:
/subsystem=elytron/filesystem-realm=ApiRealm:add-identity(identity=bence)
/subsystem=elytron/filesystem-realm=ApiRealm:set-password(identity=bence,clear={password="password123"})
/subsystem=elytron/filesystem-realm=ApiRealm:add-identity-attribute(identity=bence,name=Roles, value=["user"])

/subsystem=elytron/filesystem-realm=ApiRealm:add-identity(identity=ecneb)
/subsystem=elytron/filesystem-realm=ApiRealm:set-password(identity=ecneb,clear={password="password123"})
/subsystem=elytron/filesystem-realm=ApiRealm:add-identity-attribute(identity=ecneb,name=Roles, value=["admin","user"])

# Create a new Security Domain which maps our Realm:
/subsystem=elytron/security-domain=fsSD:add(realms=[{realm=ApiRealm,role-decoder=from-roles-attribute}],default-realm=ApiRealm,permission-mapper=default-permission-mapper)

# We need an Http Authentication Factory which references our Security Domain:
/subsystem=elytron/http-authentication-factory=example-fs-http-auth:add(http-server-mechanism-factory=global,security-domain=fsSD,mechanism-configurations=[{mechanism-name=BASIC,mechanism-realm-configurations=[{realm-name=RealmUsersRoles}]}])

# Finally, a Security Domain in the undertow’s subsystem will be associated with our Http Authentication Factory:
/subsystem=undertow/application-security-domain=ApiSecurityDomain:add(http-authentication-factory=example-fs-http-auth)

# Restart running server
reload
```





> #### cURL scripts

> API health endpoint

```bash
# Response Ok
curl -v -XGET -H 'Accept: application/xml' 'http://localhost:8080/rest/api/health'

# Response Ok
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/health'
```



> API discover endpoint

```bash
# Response Ok
curl -v -XGET -H 'Accept: application/xml' 'http://localhost:8080/rest/api/discover'

# Response Ok
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/discover'
```



> Message resource (CREATE)

```bash
# Response Forbidden
curl -v -XPOST -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"message":"Test message","year":2022}' 'http://localhost:8080/rest/api/messages'

# Response Forbidden
curl -v -XPOST -u bence:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"message":"Test message","year":2022}' 'http://localhost:8080/rest/api/messages'

# Response Ok
curl -v -XPOST -u ecneb:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"message":"Test message","year":2022}' 'http://localhost:8080/rest/api/messages'
```



> Message resource (GET ALL)

```bash
# Response Forbidden
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages'

# Response Forbidden
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages'

# Response Forbidden
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages'
```



> Message resource (UPDATE)

```bash
# Response Forbidden
curl -v -XPUT -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"message":"Test message update","year":2022}' 'http://localhost:8080/rest/api/messages/1'

# Response Forbidden
curl -v -XPUT -u bence:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"message":"Test message update","year":2022}' 'http://localhost:8080/rest/api/messages/1'

# Response Ok
curl -v -XPUT -u ecneb:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"message":"Test message update","year":2022}' 'http://localhost:8080/rest/api/messages/1'
```



> Message resource (DELETE)

```bash
# Response Forbidden
curl -v -XDELETE --cookie 'cookieId=1' -H 'headerId: 1' -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'

# Response Forbidden
curl -v -XDELETE --cookie 'cookieId=1' -u bence:password123 -H 'headerId: 1' -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'

# Response Ok
curl -v -XDELETE --cookie 'cookieId=1' -u ecneb:password123 -H 'headerId: 1' -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'
```



> Message resource (GET)

```bash
# Response Ok
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/1'
```



> Message resource (FILTER)

```bash
# Response Ok
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/filter?year=2022'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/filter?year=2022'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/messages/filter?year=2022'
```



> Profile resource (CREATE)

```bash
# Response Ok
curl -v -XPOST -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Bence","lastName":"Mate","year":2022,"messages":[{"id":1,"message":"Test message","year":2022}]}' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XPOST -u bence:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Bence","lastName":"Mate","year":2022,"messages":[{"id":1,"message":"Test message","year":2022}]}' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XPOST -u ecneb:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Bence","lastName":"Mate","year":2022,"messages":[{"id":1,"message":"Test message","year":2022}]}' 'http://localhost:8080/rest/api/profiles'
```



> Profile resource (GET ALL)

```bash
# Response Ok
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles'
```



> Profile resource (UPDATE)

```bash
# Response Ok
curl -v -XPUT -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Mate","lastName":"Bence","year":2022}' 'http://localhost:8080/rest/api/profiles/ecneb'

# Response Ok
curl -v -XPUT -u bence:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Mate","lastName":"Bence","year":2022}' 'http://localhost:8080/rest/api/profiles/ecneb'

# Response Ok
curl -v -XPUT -u ecneb:password123 -H 'Accept: application/json' -H 'Content-type: application/json' -d '{"id":1,"profileName":"ecneb","firstName":"Mate","lastName":"Bence","year":2022}' 'http://localhost:8080/rest/api/profiles/ecneb'
```



> Profile resource (DELETE)

```bash
# Response Forbidden
curl -v -XDELETE -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb;matrixName=ecneb'

# Response Ok
curl -v -XDELETE -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb;matrixName=ecneb'

# Response Ok
curl -v -XDELETE -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb;matrixName=ecneb'
```



> Profile resource (GET)

```bash
# Response Forbidden
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb'
```



> Profile resource (FILTER)

```bash
# Response Forbidden
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/filter/2022'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/filter/2022'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/filter/2022'
```



> Profile resource (SUBRESOURCE)

```bash
# Response Forbidden
curl -v -XGET -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb/messages/1'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb/messages/1'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/json' 'http://localhost:8080/rest/api/profiles/ecneb/messages/1'
```



> Profile resource (CONTENT NEGOTIATION)

```bash
# Response Forbidden
curl -v -XGET -H 'Accept: application/xml' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XGET -u bence:password123 -H 'Accept: application/xml' 'http://localhost:8080/rest/api/profiles'

# Response Ok
curl -v -XGET -u ecneb:password123 -H 'Accept: application/xml' 'http://localhost:8080/rest/api/profiles'
```