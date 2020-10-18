package space.devport.wertik.milestones.system.storage.mysql;

public enum Query {

    CREATE_RECORD_TABLE("CREATE TABLE IF NOT EXISTS `%table%` (\n"
            + "    `uuid` VARCHAR(36) NOT NULL ,\n"
            + "    `score` BIGINT NOT NULL DEFAULT 0 ,\n"
            + "    PRIMARY KEY (`uuid`), \n"
            + "    UNIQUE (`uuid`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"),

    UPDATE_RECORD("INSERT INTO `%table%` (`uuid`, `score`) " +
            "VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "`uuid` = ?, `score` = ?"),

    GET_RECORD("SELECT `uuid`, `score` FROM `%table%` WHERE `uuid` = ?"),

    DELETE_RECORD("DELETE FROM `%table%` WHERE `uuid` = ?");

    private final String statement;

    Query(String statement) {
        this.statement = statement;
    }

    public String get(String table) {
        return this.statement.replace("%table%", table);
    }
}