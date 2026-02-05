package $package$.application

import scala.util.matching.Regex

object RegexMatchService {

  def findFirstMatch(pattern: String, input: CharSequence): Option[String] = {
    val regex = new Regex(pattern)
    //CWE-1333
    //SINK
    regex.findFirstIn(input)
  }
}
