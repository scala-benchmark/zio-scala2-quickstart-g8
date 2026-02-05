package $package$.application

import $package$.application.RegexMatchService

object PatternMatchService {

  def matchPattern(pattern: String, input: CharSequence): Option[String] =
    RegexMatchService.findFirstMatch(pattern, input)
}
