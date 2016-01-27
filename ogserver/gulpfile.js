var gulp = require('gulp');
var uglify = require('gulp-uglify');
var clean = require('gulp-clean');
var cssnano = require('gulp-cssnano');

gulp.task('clean', function () {
	return gulp.src('build/webroot', {read: false})
		.pipe(clean());
});

gulp.task('dist', ['clean'], function() {
  return gulp.src('src/main/resources/webroot/**/*')
    .pipe(gulp.dest('build/webroot'));
});

gulp.task('uglify', ['dist'], function() {
  return gulp.src(['build/webroot/js/**/*.js', '!build/webroot/js/libs/**/*.js'])
    .pipe(uglify())
    .pipe(gulp.dest('build/webroot/js'));
});

gulp.task('cssnano', ['dist'], function() {
    return gulp.src('build/webroot/css/**/*.css')
        .pipe(cssnano({safe: true}))
        .pipe(gulp.dest('build/webroot/css'));
});

gulp.task('default', ['uglify', 'cssnano']);

