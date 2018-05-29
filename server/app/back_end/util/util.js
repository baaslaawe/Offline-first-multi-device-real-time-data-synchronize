exports.version = "0.1.0";

exports.newInstance = function() {
    return new Util();
};

function Util() {
}

exports.current_timestamp = function() {
    return Math.floor( new Date().getTime() / 1000 );
};

exports.now = function() {
    return new Date();
};