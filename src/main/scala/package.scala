package object qiita_twitter_bot{

  @inline def allCatchPrintStackTrace(body: => Any){
    try{
      val r = body
    }catch{
      case e: Throwable => e.printStackTrace
    }
  }

  type ITEM_URL = String
}
