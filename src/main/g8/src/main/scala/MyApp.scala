import dispatch._

object MyApp {
  def main(args: Array[String]) {
    val svc = url("http://en.wikipedia.org/w/api.php")
    val rec = svc <<? Map("action" -> "query",
                          "list" -> "recentchanges",
                          "rcprop" -> "title",
                          "rclimit" -> "1",
                          "format" -> "xml")
    val recent =
      for (xml <- Http.default(rec OK as.xml.Elem))
        yield for {
          elem <- xml \\\\ "rc"
          attr <- elem.attribute("title") 
        } yield attr.toString
    val last = for (titles <- recent)
      yield titles.headOption.getOrElse("-nothing-")
    println("The last Wikipedia edit was to '%s'".format(last()))

    Http.shutdown()
  }
}
