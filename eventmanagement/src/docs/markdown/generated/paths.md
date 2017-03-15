## Paths
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

### postCollectionResource
```
POST /api/files
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|payload|payload|true|File||


#### Responses
|HTTP Code|Description|Schema|
|----|----|----|
|200|OK|Resource«File»|
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

### getCollectionResource
```
GET /api/files
```

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

### listSearches
```
GET /api/files/search
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

* File Entity Search

### optionsForSearches
```
OPTIONS /api/files/search
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

* File Entity Search

### headForSearches
```
HEAD /api/files/search
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

* File Entity Search

### /getByEventId
```
GET /api/files/search/getByEventId
```

#### Parameters
|Type|Name|Description|Required|Schema|Default|
|----|----|----|----|----|----|
|BodyParameter|param0|param0|false|Event||


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

### getCollectionResourceCompact
```
GET /api/users
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

