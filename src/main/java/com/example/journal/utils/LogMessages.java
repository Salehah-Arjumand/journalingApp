package com.example.journal.utils;

public class LogMessages {
    private LogMessages() {}

    // Generic
    public static final String ACCESS_DENIED = "Access denied.";

    // Health check
    public static final String HEALTH_CHECK = "Calling health check.";

    // User logs
    public static final String USER_WITH_ID_NOT_FOUND = "User with ID: {} not found.";
    public static final String USER_WITH_USERNAME_NOT_FOUND = "User with username: {} not found.";
    public static final String USER_WITH_ID_EXISTS = "User with ID: {} already exists.";
    public static final String USER_WITH_USERNAME_EXISTS = "User with username: {} already exists.";
    public static final String CREATE_USER = "Creating user with username: {}.";
    public static final String GET_USER_BY_ID = "Getting user with ID: {}.";
    public static final String GET_USER_BY_USERNAME = "Getting user with username: {}.";
    public static final String DELETE_USER_BY_ID = "Deleting user with ID: {}.";
    public static final String DELETE_USER_BY_USERNAME = "Deleting user with username: {}.";
    public static final String UPDATE_USER_BY_USERNAME = "Updating user with username: {}.";
    public static final String UPDATE_USER_BY_ID = "Updating user with ID: {}.";

    // Admin logs
    public static final String CREATE_ADMIN = "Creating admin with username: {}.";

    // Journal entry logs
    public static final String CREATE_JOURNAL = "Creating journal with title: {} for user with username: {}.";
    public static final String GET_ALL_JOURNALS_BY_USER = "Getting all journal entries by user with username: {}.";
    public static final String GET_JOURNAL_BY_ID_AND_USER = "Getting journal entry with ID: {} by user with username: {}.";
    public static final String JOURNAL_NOT_FOUND_FOR_USER = "Journal entry with ID: {} does not exist for user with username: {}.";
    public static final String DELETE_JOURNAL_BY_ID_AND_USER = "Deleting journal entry with ID: {} for user with username: {}.";
    public static final String UPDATE_JOURNAL_BY_ID_AND_USER = "Updating journal entry with ID: {} for user with username: {}.";
    public static final String JOURNAL_WITH_ID_EXISTS = "Journal with ID: {} already exists.";

}
