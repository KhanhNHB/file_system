package com.khanhnhb.filesystem.commands

import com.khanhnhb.filesystem.file.{DirEntry, Directory}
import com.khanhnhb.filesystem.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
  override def apply(state: State): State = {
    /*
    cd /something/somethingElse/../
    cd a/b/c = relative to the current working directory

    cd ..
    cd ..
     */

    // 1. Find the root
    val root = state.root
    val wd = state.wd

    // 2. Find the absolute path of the directory I want to cd to
    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir

    // 3. Find the directory to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4. Change the state given the new directory
    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(s"$dir: not such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      /*
        /a/b => ["a", "b"]

        path.isEmpty?
            collapseRelativeTokens(["b"], result = List :+ ["a"])
              path.isEmpty?
                collapseRelativeTokens([], result = ["a"] :+ ["b"])
                  path.isEmpty?
----------------------------------------------------------------------------
        /a/.. => ["a", ".."]

        path.isEmpty?
            collapseRelativeTokens([..], [] :+ "a" = ["a"])
              path.isEmpty?
                collapseRelativeTokens([], [])
----------------------------------------------------------------------------
        /a/b.. => ["a", "b", ".."]

        path.isEmpty?
            collapseRelativeTokens(["b", ".."], ["a"])
              path.isEmpty?
                collapseRelativeTokens([..], ["a", "b"])
                  path.isEmpty?
                    collapseRelativeTokens([], ["b"])

----------------------------------------------------------------------------
        /a/b/c/.. => ["a", "b", "c", ".."]

        ...
          collapseRelativeTokens([..], ["a", "b", "c"])
            path.isEmpty?

       */

      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      } else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    // 1. Tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    // 1.5 eliminate/collapse relative tokens
    /*
      ["a", "."] => ["a"]
      ["a", "b", ".", "."] => ["a", "b"]

      /a/../ => ["a", ".."] => []
      /a/b/.. => ["a", "b", ".."] => ["a"]
     */
    val newTokens = collapseRelativeTokens(tokens, List())

    // 2. Navigate to the correct entry
    if (newTokens == null) null
    else findEntryHelper(root, newTokens)
  }
}
