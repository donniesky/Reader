package me.donnie.reader.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

abstract class InitProvider : ContentProvider() {
  
  override fun insert(uri: Uri, values: ContentValues?): Uri? = unsupported()
  
  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?
  ): Cursor? = unsupported()
  
  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<out String>?
  ): Int = unsupported()
  
  override fun delete(uri: Uri, selection: String?,
                      selectionArgs: Array<out String>?): Int = unsupported()
  
  override fun getType(uri: Uri): String? = unsupported()
}