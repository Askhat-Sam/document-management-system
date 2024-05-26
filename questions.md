--Features to be included ---

#Users:
- sorting (by different columns) - DONE
- reverse sorting

#Documents:
- sorting (by different columns) - DONE
- reverse sorting

---------------------------------------------------------------------------------------------------------------------

-- Available endpoints ---

//Getting all documents
http://localhost:8080/document-management/documents/getDocuments

//Getting all documents sorted by column revision (replace sortBy value for required column name). Defaulting sorting direction is ascending.
http://localhost:8080/document-management/documents/getDocuments?sortBy=revision

//Getting all documents sorted by column revision (replace sortBy value for required column name) and sorting direction 
descending (for ascending sorting replace to sorting direction to A)
http://localhost:8080/document-management/documents/getDocuments?sortBy=revision&sortDirection=D

//Getting the list of documents located on page 2 and size 1
http://localhost:8080/document-management/documents/getDocuments?page=2&size=1

//Getting one document by id
http://localhost:8080/document-management/documents/getDocument/3

//Adding new document
http://localhost:8080/document-management/documents/addNewDocument?documentReference=POL_008&type=Policy&name=OHS policy&status=In progress&revision=2&issueDate=02.05.2022&revisionDate=04.09.2022&revisionInterval=365&processOwner=OHS department

//Updating the document by id
http://localhost:8080/document-management/documents/updateDocument?id=3&documentReference=POL-6666

//Deleting the document by id
http://localhost:8080/document-management/documents/deleteDocument?id=7
---------------------------------------------------------------------------------------------------------------------

//Get all users
http://localhost:8080/document-management/users/getUsers

//Getting all users sorted by column id (replace sortBy value for required column name). Defaulting sorting direction is ascending.
http://localhost:8080/document-management/users/getUsers?sortBy=id

//Getting all users sorted by column id (replace sortBy value for required column name) and sorting direction
descending (for ascending sorting replace to sorting direction to D)
http://localhost:8080/document-management/users/getUsers?sortBy=id&sortDirection=A

//Getting the list of users located on page 2 and size 1
http://localhost:8080/document-management/users/getUsers?page=0&size=1

//Getting one user by id 
http://localhost:8080/document-management/users/getUser/2

//Adding new user
http://localhost:8080/document-management/users/addNewUser?userId=Stephen.K&firstName=Stephen&lastName=King&email=Stephen.King@gmail.cpm&password=123&active=1&roleId=Stephen.K&userRole=ROLE_MANAGER

//Updating the user by id
http://localhost:8080/document-management/documents/updateDocument?id=3&documentReference=POL-777

//Deleting the user by id
http://localhost:8080/document-management/users/deleteUser?id=9
