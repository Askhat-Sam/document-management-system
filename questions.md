--Features to be included ---

#Users:
- implement sorting (by different columns) - DONE
- implement pagination - DONE
- implement reverse sorting - DONE
- implement search for user - DONE
- implement download of list of ALL users - DONE
- how drop count of id after deleting the user - ?????????????
- BCRYPT not working fo the new or updated users - ?

#Documents:
- implement sorting (by different columns) - DONE
- implement reverse sorting - DONE
- implement pagination - DONE 
- move pagination as separate class -?
- add link field to document - DONE
- simulate pdf upload (copying file from "src/main/resources/documentSource" to "src/main/resources/documentsUploaded" folder) - DONE
- simulate pdf delete (deleting file from  "src/main/resources/documentsUploaded" folder) - DONE
- implement document comment entity - DONE
- implement addComment method to add comments to the documents - DONE
- exception handling fod bad request (e.g request to delete user which is not exist in DB) - ?
- implement search for documents - DONE
- implement download of list of ALL document - DONE
- to apply filter to the list of documents
---------------------------------------------------------------------------------------------------------------------

-- Available endpoints ---

// GET - Getting all documents
http://localhost:8080/document-management/documents/getDocuments

// GET - Getting all documents sorted by column revision (replace sortBy value for required column name). Defaulting sorting direction is ascending.
http://localhost:8080/document-management/documents/getDocuments?sortBy=revision

// GET - Getting all documents sorted by column revision (replace sortBy value for required column name) and sorting direction 
descending (for ascending sorting replace to sorting direction to A)
http://localhost:8080/document-management/documents/getDocuments?sortBy=revision&sortDirection=D

// GET - Getting the list of documents with pagination (list of documents located on page 2 and size 1)
http://localhost:8080/document-management/documents/getDocuments?page=2&size=1

// GET - Getting one document by id
http://localhost:8080/document-management/documents/getDocument/3

// POST - Adding new document
http://localhost:8080/document-management/documents/addNewDocument?documentCode=POL-666&documentTypeId=1&name=OHS policy 2&revisionNumber=1&statusId=2&creationDate=02.05.2022&modificationDate=04.09.2022&authorId=2&link=src/main/resources/docuemntSource/newFile2.txt

// POST - Updating the document by id
http://localhost:8080/document-management/documents/updateDocument?id=2&documentCode=INS-002

// GET - Deleting the document by id
http://localhost:8080/document-management/documents/deleteDocument?id=7

// POST - Add comment to the document
http://localhost:8080/document-management/documents/addComment?documentId=3&userId=Sarah.S&comment=Typo

// GET - Search for documents by keyword (e.g Policy) and in certain column (e.g type)
http://localhost:8080/document-management/documents/getDocuments?keyword=Policy&column=type

// GET - Download the list of all documents
http://localhost:8080/document-management/documents/downloadList

// GET - Download the one documetn by ID
http://localhost:8080/document-management/documents/downloadDocument?id=1

// POST - Add new revision of the document
http://localhost:8080/document-management/documents/addRevision?userId=3&documentId=3&date=01.01.2024&revisionNumber=2&statusId=1&description=New revision of the new document&link=Some link to document&validatingUserId=1
---------------------------------------------------------------------------------------------------------------------

// GET - Get all users
http://localhost:8080/document-management/users/getUsers

// GET - Getting all users sorted by column id (replace sortBy value for required column name). Defaulting sorting direction is ascending.
http://localhost:8080/document-management/users/getUsers?sortBy=id

// GET - Getting all users sorted by column id (replace sortBy value for required column name) and sorting direction
descending (for ascending sorting replace to sorting direction to D)
http://localhost:8080/document-management/users/getUsers?sortBy=id&sortDirection=A

// GET - Getting the list of users paginated (list of users located on page 2 and size 1)
http://localhost:8080/document-management/users/getUsers?page=0&size=1

// GET - Getting one user by id 
http://localhost:8080/document-management/users/getUser/2

// POST - Adding new user
http://localhost:8080/document-management/users/addNewUser?userId=Stephen.K&firstName=Stephen&lastName=King&email=Stephen.King@gmail.cpm&departmentId=4&role=ROLE_MANAGER&password=fun123

// POST - Updating the user by id
http://localhost:8080/document-management/documents/updateDocument?id=3&documentReference=POL-777

// GET - Deleting the user by id
http://localhost:8080/document-management/users/deleteUser?id=9

// GET - Search for user by keyword (e.g Policy) and in certain column (e.g type)
http://localhost:8080/document-management/users/getUsers?keyword=john&column=firstName

// GET - Download the list of all users
http://localhost:8080/document-management/users/downloadList


