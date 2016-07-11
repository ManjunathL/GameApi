var gulp = require('gulp');
var uglify = require('gulp-uglify');
var cssnano = require('gulp-cssnano');
var concat = require ('gulp-concat-css');

gulp.task('uglifyjs', function() {
  return gulp.src(['build/resources/main/webroot/js/**/*.js', '!build/resources/main/webroot/js/libs/**/*.js'])
    .pipe(uglify())
    .pipe(gulp.dest('build/resources/main/webroot/js'));
});

gulp.task('css', function(){
    return gulp.src([
    'build/resources/main/webroot/js/libs/bootstrap-custom/css/bootstrap.min.css',
    'build/resources/main/webroot/js/libs/bootstrap-custom/css/bootstrap-theme.min.css',
    'build/resources/main/webroot/js/libs/jquery-ui/themes/base/jquery-ui.min.css',
    'build/resources/main/webroot/css/agency.css',
    'build/resources/main/webroot/css/horizontal.css',
    'build/resources/main/webroot/css/font-awesome.min.css',
    'build/resources/main/webroot/fonts/pixeden/pe-icon-7-stroke/css/pe-icon-7-stroke.css',
    'build/resources/main/webroot/fonts/pixeden/pe-icon-7-stroke/css/helper.css'
], { base: 'build/resources/main/webroot/css' })
.pipe(concat('single.css'))
.pipe(cssnano({safe: true}))
.pipe(gulp.dest('build/resources/main/webroot/css'));
});

gulp.task('default', ['uglifyjs', 'css']);

