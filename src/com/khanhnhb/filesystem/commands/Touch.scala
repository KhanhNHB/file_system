package com.khanhnhb.filesystem.commands
import com.khanhnhb.filesystem.file.{DirEntry, File}
import com.khanhnhb.filesystem.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
