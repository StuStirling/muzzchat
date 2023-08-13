# Assumptions

- Chats are only ever going to be one to one.
- Amount of messages in a chat would not cause memory issues
- Displayed chat would only ever be between the two, hardcoded users.
- Messages would only be plain text with no character limit.
- Local storage encryption is not needed
- UI testing is not required

# Decisions

- **User differentiation** - Differentiated the current user by a flag on the users object. This was for speed. No doubt an actual current user would contain a lot more information so would warrant a different data structure.
- **Database structure** - Used a simple double foreign key on the message to attribute the chats to users. This was for speed. A more flexible way would be to have an association table, `chats` maybe, to tie users and messages together.
- **Message pagination** - There is no pagination on the message retrieval. Again this was for speed but it would most likely be needed for a real implementation. 
- **Sending message state** - There is no status or error handling on the sending of the message. This could fail or take a long time so loading and failure states should be handled given more time.
- **Message Rerieval** - Message failure and retrieval handling could be handled independently of the user retrieval as it could be more IO intensive and flaky. UX would probably be improved as retry capabilities could be added.
- **General failure handling** - Failures are currently just displayed as generic messages. I like to handle the mapping of IO and data specific errors in the data layer as the UIlayer should not know about the implementation details.
- **Modules** - Currently the code is separated into packages. These would normally be separated into modules, backed by gradle convention plugins to follow the DRY principle.
- **No encryption** - Normally you would want to encrypt the database using something like SQLCipher. Alternatively you could possibly provide your own implementation by implementing a custom serializer or deserializer (I've done this for datastore, not for Room).

