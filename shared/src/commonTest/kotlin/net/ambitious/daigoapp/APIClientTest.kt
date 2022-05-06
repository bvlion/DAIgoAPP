package net.ambitious.daigoapp

expect class APIClientTest {

    /** Check default host */
    fun testHost()

    /** Test 4 connect */
    fun testNoAuthClient()

    /** Test 4 connect and header */
    fun testAuthClient()

    /** Test dispatcher */
    fun testDispatcher()
}