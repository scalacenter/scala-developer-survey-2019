var config = require("./scalajs.webpack.config");
var UglifyJsPlugin = require("uglifyjs-webpack-plugin");

config.optimization = {
  minimizer: [ new UglifyJsPlugin({ sourceMap: false }) ]
};

module.exports = config;
