package com.ums.schedule.repository;

public enum DbErrorMessage {
   NOT_NULL_CONSTRAINT("NULL not allowed for column"),
   UNIQUE_CONSTRAINT("Unique index or primary key violation") ;

   String errorMessage;

   DbErrorMessage(String errorMessage) {
       this.errorMessage = errorMessage;
   }

   public String getMessage() {
      return errorMessage;
   }
}
