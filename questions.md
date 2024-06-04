--Features to be included ---

#Users:
- implement sorting (by different columns) - DONE
- implement pagination - DONE
- implement reverse sorting - DONE
- implement search for user - DONE
- implement download of list of ALL users - ?
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
http://localhost:8080/document-management/documents/addNewDocument?documentReference=POL_008&type=Policy&name=OHS policy&status=In progress&revision=2&issueDate=02.05.2022&revisionDate=04.09.2022&revisionInterval=365&processOwner=OHS department&link=src/main/resources/docuemntSource/file2.txt

// POST - Updating the document by id
http://localhost:8080/document-management/documents/updateDocument?id=3&documentReference=INS-002

// GET - Deleting the document by id
http://localhost:8080/document-management/documents/deleteDocument?id=7

// POST - Add comment to the document
http://localhost:8080/document-management/documents/addComment?documentId=3&userId=Sarah.S&comment=Typo

// GET - Search for documents by keyword (e.g Policy) and in certain column (e.g type)
http://localhost:8080/document-management/documents/getDocuments?keyword=Policy&column=type

// GET - Download the list of all documents
http://localhost:8080/document-management/documents/downloadList

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
http://localhost:8080/document-management/users/addNewUser?userId=Stephen.K&firstName=Stephen&lastName=King&email=Stephen.King@gmail.cpm&password=123&active=1&roleId=Stephen.K&userRole=ROLE_MANAGER

// POST - Updating the user by id
http://localhost:8080/document-management/documents/updateDocument?id=3&documentReference=POL-777

// GET - Deleting the user by id
http://localhost:8080/document-management/users/deleteUser?id=9

// GET - Search for documents by keyword (e.g Policy) and in certain column (e.g type)
http://localhost:8080/document-management/users/getUsers?keyword=john&column=firstName
