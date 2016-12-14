var gulp = require('gulp');
var cssnano = require('gulp-cssnano');
var concat = require ('gulp-concat-css');

var exec = require('child_process').exec;

gulp.task('optimizejs', function (cb) {
  return exec('node build/resources/main/build/r.js -o build/resources/main/build/all.build.js', function (err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    cb(err);
  });
})

gulp.task('css', function(){
    return gulp.src([
    'build/resources/main/webroot/js/libs/bootstrap-custom/css/bootstrap.min.css',
    'build/resources/main/webroot/js/libs/bootstrap-custom/css/bootstrap-theme.min.css',
    'build/resources/main/webroot/js/libs/jquery-ui/themes/base/jquery-ui.min.css',
    'build/resources/main/webroot/css/agency.css',
    'build/resources/main/webroot/css/style.css',
    'build/resources/main/webroot/css/animate.css',
    'build/resources/main/webroot/css/festive.css',
    'build/resources/main/webroot/css/consult.css',
    'build/resources/main/webroot/css/shortlist.css',
    'build/resources/main/webroot/css/horizontal.css',
    'build/resources/main/webroot/css/kyk.css',
    'build/resources/main/webroot/css/kyw.css',
    'build/resources/main/webroot/css/ml.css',
    'build/resources/main/webroot/css/owl.transitions.css',
    'build/resources/main/webroot/css/font-awesome.css',
    'build/resources/main/webroot/css/font-awesome.min.css',
    'build/resources/main/webroot/fonts/pixeden/pe-icon-7-stroke/css/pe-icon-7-stroke.css',
    'build/resources/main/webroot/fonts/pixeden/pe-icon-7-stroke/css/helper.css',
    'build/resources/main/webroot/js/libs/owl.carousel/dist/assets/owl.theme.default.min.css',
    'build/resources/main/webroot/js/libs/owl.carousel/dist/assets/owl.carousel.css',
    'build/resources/main/webroot/js/libs/highlight/highlight.css'
], { base: 'build/resources/main/webroot/css' })
.pipe(concat('single.css'))
.pipe(cssnano({safe: true}))
.pipe(gulp.dest('build/resources/main/webroot/css'));
});

gulp.task('default', ['optimizejs', 'css']);


