## Definitions
### AbstractJsonSchemaProperty«object»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|readOnly||false|boolean||
|title||false|string||


### Event
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|file array||
|id||false|integer (int64)||
|name||false|string||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### File
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|created||false|string (date-time)||
|event||false|Event||
|id||false|integer (int64)||
|name||false|string||
|updated||false|string (date-time)||


### HttpEntity
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|body||false|object||


### Item
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|properties||false|object||
|requiredProperties||false|Collection«string»||
|type||false|string||


### JsonSchema
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|$schema||false|string||
|definitions||false|object||
|description||false|string||
|properties||false|object||
|requiredProperties||false|Collection«string»||
|title||false|string||
|type||false|string||


### Link
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|href||false|string||
|rel||false|string||
|templated||false|boolean||


### ModelAndView
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|empty||false|boolean||
|model||false|object||
|modelMap||false|object||
|reference||false|boolean||
|status||false|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)||
|view||false|View||
|viewName||false|string||


### RepositoryLinksResource
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|links||false|Link array||


### RepositorySearchesResource
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|links||false|Link array||


### ResourceSupport
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|links||false|Link array||


### Resources«Event»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«Event»||
|links||false|Link array||


### Resources«File»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«File»||
|links||false|Link array||


### Resources«Link»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«Link»||
|links||false|Link array||


### Resources«User»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«User»||
|links||false|Link array||


### Resource«Event»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|file array||
|id||false|integer (int64)||
|links||false|Link array||
|name||false|string||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### Resource«File»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|created||false|string (date-time)||
|event||false|Event||
|id||false|integer (int64)||
|links||false|Link array||
|name||false|string||
|updated||false|string (date-time)||


### Resource«User»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|lastName||false|string||
|links||false|Link array||


### User
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|lastName||false|string||


### View
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|contentType||false|string||


