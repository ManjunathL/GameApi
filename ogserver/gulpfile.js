var gulp = require('gulp');
var uglify = require('gulp-uglify');
var cssnano = require('gulp-cssnano');

gulp.task('uglify', function() {
  return gulp.src(['src/main/resources/webroot/js/**/*.js', '!src/main/resources/webroot/js/libs/**/*.js'])
    .pipe(uglify())
    .pipe(gulp.dest('build/resources/main/webroot/js'));
});

gulp.task('cssnano', function() {
  return gulp.src('src/main/resources/webroot/css/**/*.css')
    .pipe(cssnano({safe: true}))
    .pipe(gulp.dest('build/resources/main/webroot/css'));
});

gulp.task('default', ['uglify', 'cssnano']);

