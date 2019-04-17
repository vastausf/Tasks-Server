object TokenStore {

    fun createToken(userData: UserDataFull): String {
        return "${userData.id}:" + "${userData.id}${userData.login}${userData.password}".getHashSHA256().toUpperCase()
    }

}