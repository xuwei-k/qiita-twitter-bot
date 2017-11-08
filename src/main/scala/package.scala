package object qiita_twitter_bot {

  @inline def allCatchPrintStackTrace(body: => Any): Unit = {
    try {
      val _ = body
    } catch {
      case e: Throwable =>
        Main.printDateTime()
        e.printStackTrace()
    }
  }

  type ITEM_URL = String
}
