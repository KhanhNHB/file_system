package com.khanhnhb.filesystem.file

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  val ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = {
    new Directory(parentPath, name, List())
  }
}
