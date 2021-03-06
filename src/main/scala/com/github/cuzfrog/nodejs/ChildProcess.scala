package com.github.cuzfrog.nodejs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
@JSImport("child_process", JSImport.Namespace)
object ChildProcess extends js.Object {
  def execSync(command: String, options: js.UndefOr[js.Object] = js.undefined): js.Object | String = js.native
}