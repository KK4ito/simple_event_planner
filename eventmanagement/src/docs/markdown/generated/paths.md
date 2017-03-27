## Paths
### display
```
GET /api/download/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|string (byte) array|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* */*

#### Tags

* file-repository

### headCollectionResource
```
HEAD /api/eventAttendees
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### getCollectionResourceCompact
```
GET /api/eventAttendees
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Link»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* text/uri-list
* application/x-spring-data-compact+json

#### Tags

* EventAttendee Entity

### postCollectionResource
```
POST /api/eventAttendees
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|EventAttendee||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resource«EventAttendee»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### optionsForCollectionResource
```
OPTIONS /api/eventAttendees
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### headForItemResource
```
HEAD /api/eventAttendees/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### patchItemResource
```
PATCH /api/eventAttendees/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|EventAttendee||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«EventAttendee»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### getItemResource
```
GET /api/eventAttendees/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«EventAttendee»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### putItemResource
```
PUT /api/eventAttendees/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|EventAttendee||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«EventAttendee»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### deleteItemResource
```
DELETE /api/eventAttendees/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«EventAttendee»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### optionsForItemResource
```
OPTIONS /api/eventAttendees/{id}
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* EventAttendee Entity

### headCollectionResource
```
HEAD /api/events
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### getCollectionResourceCompact
```
GET /api/events
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Link»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* text/uri-list
* application/x-spring-data-compact+json

#### Tags

* Event Entity

### postCollectionResource
```
POST /api/events
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|Event||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resource«Event»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### optionsForCollectionResource
```
OPTIONS /api/events
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### headForItemResource
```
HEAD /api/events/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### patchItemResource
```
PATCH /api/events/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|Event||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Event»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### getItemResource
```
GET /api/events/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Event»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### putItemResource
```
PUT /api/events/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|Event||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Event»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### deleteItemResource
```
DELETE /api/events/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Event»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### optionsForItemResource
```
OPTIONS /api/events/{id}
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* Event Entity

### headCollectionResource
```
HEAD /api/files
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### getCollectionResourceCompact
```
GET /api/files
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«Link»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* text/uri-list
* application/x-spring-data-compact+json

#### Tags

* File Entity

### post
```
POST /api/files
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|FormDataParameter|file|file|true|file||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|file|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* multipart/form-data

#### Produces

* */*

#### Tags

* file-repository

### optionsForCollectionResource
```
OPTIONS /api/files
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### /unusedFiles
```
GET /api/files/search/unusedFiles
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Iterable«File»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json
* application/x-spring-data-compact+json

#### Tags

* File Entity Search

### headForItemResource
```
HEAD /api/files/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### patchItemResource
```
PATCH /api/files/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|File||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«File»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### getItemResource
```
GET /api/files/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«File»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### putItemResource
```
PUT /api/files/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|File||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«File»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### deleteItemResource
```
DELETE /api/files/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«File»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### optionsForItemResource
```
OPTIONS /api/files/{id}
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* File Entity

### headCollectionResource
```
HEAD /api/users
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### getCollectionResource
```
GET /api/users
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«User»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### postCollectionResource
```
POST /api/users
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|User||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resource«User»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### optionsForCollectionResource
```
OPTIONS /api/users
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### headForSearches
```
HEAD /api/users/search
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|HttpEntity|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity Search

### listSearches
```
GET /api/users/search
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|RepositorySearchesResource|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity Search

### optionsForSearches
```
OPTIONS /api/users/search
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|HttpEntity|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity Search

### /find
```
GET /api/users/search/find
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|param0|param0|false|integer||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Iterable«User»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json
* application/x-spring-data-compact+json

#### Tags

* User Entity Search

### headForItemResource
```
HEAD /api/users/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### patchItemResource
```
PATCH /api/users/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|User||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«User»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### getItemResource
```
GET /api/users/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«User»|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### putItemResource
```
PUT /api/users/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|User||
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«User»|
|201|Created|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|
|404|Not Found|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### deleteItemResource
```
DELETE /api/users/{id}
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|PathParameter|id|id|true|integer (int64)||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resources«User»|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

### optionsForItemResource
```
OPTIONS /api/users/{id}
```

#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|No Content|
|204|No Content|No Content|
|401|Unauthorized|No Content|
|403|Forbidden|No Content|


#### Consumes

* application/json

#### Produces

* application/*+json;charset=UTF-8
* application/json
* application/hal+json

#### Tags

* User Entity

