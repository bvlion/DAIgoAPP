package net.ambitious.daigoapp.android.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DaiGoTest {

  @Test
  fun isSuccessTrueWhenSaveIsSuccess() {
    assertTrue(DaiGo.UpdateResponse("success").isSuccess)
  }

  @Test
  fun isSuccessFalseWhenSaveIsNotSuccess() {
    assertFalse(DaiGo.UpdateResponse("failed").isSuccess)
  }
}
