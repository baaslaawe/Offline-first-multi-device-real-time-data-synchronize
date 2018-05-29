var config = {};

config = {
    db_config: {
        host: "localhost", // "localhost"
        port: 3306,
        user: "root", // "root"
        password: "", // ""
        database: "sync_demo",

        pooled_connections: 2000,
        idle_timeout_millis: 30000
    }
};

exports.config = config;
