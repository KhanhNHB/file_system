package com.khanhnhb.filesystem.commands
import com.khanhnhb.filesystem.file.{DirEntry, Directory}
import com.khanhnhb.filesystem.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}