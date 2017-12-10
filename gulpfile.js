const gulp = require('gulp');
const del = require("del");
const less = require("gulp-less");
const rename = require("gulp-rename");
const cleanCSS = require("gulp-clean-css");
const concat = require("gulp-concat");

gulp.task('less', function () {
    return gulp.src("app/assets/styles/**/*.less")
        .pipe(less())
        .pipe(concat("style.css"))
        .pipe(rename({suffix: ".min"}))
        .pipe(cleanCSS())
        .pipe(gulp.dest("app/assets/styles/"))
});

gulp.task('dev', function () {
    gulp.watch('app/assets/styles/**/*.less', ['less']);
});