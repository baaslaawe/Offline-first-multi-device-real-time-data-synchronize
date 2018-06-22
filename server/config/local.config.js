var config = {};

config = {
    db_config: {
        host: "localhost", // ""
        port: 3306,
        user: "", // "root"
        password: "", // ""
        database: "",

        pooled_connections: 2000,
        idle_timeout_millis: 30000
    }
};

exports.config = config;
