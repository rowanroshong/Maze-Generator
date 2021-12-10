#lang racket
(provide blank_maze draw_border draw_line draw_wall fill_cell save_image show_image)
(require racket/draw)
(require racket/gui)

;; Some useful(?) drawing functions:
;; a handle is a (dc bitmap m n), where bitmap is an image object and dc is a canvas
;;                                object the draw library uses to modify the image
;;
;; (blank_maze rows cols) --> returns handle for an m-by-n unit maze w/offset
;; (draw_border handle) -> draws a border (which has side effect of white bg instead of transparent)
;; (draw_line handle row1 col1 row2 col2) -> draws given line: (row1 col1) -> (row2 col2)
;; (draw_wall handle row col dir) -> draws the given unit-length wall of the cell at row col
;;                                   dir is 't, 'b, 'l, 'r
;; (fill_cell handle row col color) -> fills the cell whose upper-left corner is (row col) with a given color square
;; (show_image handle) -> displays image in interactions window
;; (save_image handle filename) -> saves image as a .png image as filename (new file or overwriting existing file)

;; some global variables used
(define DRAW-SCALE 20) ; pixels of a unit, must be >= 5 for other functions to work, 20 works well up to 50-by-50.
                       ; If on a high-resolution display (like MacBooks with retina displays), this is true pixels,
                       ; not scaled pixels, so you may want to scale up 2x or so...
(define OFFSET 1) ; number of units of border around image

;; blank_maze
;; ----------------
;; Constructs a list of useful items to use in drawing. Initializes drawing
;; by creating a 1-pixel border around entire image.
;;
;; Inputs: rows - pos. int
;;         cols - pos. int
;;
;; Returns: list of: canvas - draw library dc object
;;                   image - draw library bitmap object
;;                   rows - as input
;;                   cols - as input
(define (blank_maze rows cols)
  (define image (make-bitmap (* DRAW-SCALE (+ OFFSET OFFSET cols)) (* DRAW-SCALE (+ OFFSET OFFSET rows))))
  (define canvas (new bitmap-dc% [bitmap image]))
  (define handle (list canvas image rows cols))
  (draw_border handle)
  handle)

;; draw_border
;; ----------------
;; Draws a rectangle around perimeter of image, coloring interior white.
;;
;; Inputs: handle - handle list
;;
;; Returns: none
;;
;; Side effects: modifies bitmap (second handle)
(define (draw_border handle)
  (send (first handle) set-brush "white" 'solid)
  (send (first handle) set-pen "black" 1 'solid)
  (send (first handle) draw-rectangle ;(start-col start-row width height)
        0 0
        (* DRAW-SCALE (+ OFFSET OFFSET (fourth handle))) (* DRAW-SCALE (+ OFFSET OFFSET (third handle)))))

;; draw_line
;; ----------------
;; Draws a line from (row1 col1) to (row2 col2).
;;
;; Inputs: handle - handle list
;;         row1 - non-neg. int
;;         col1 - non-neg. int
;;         row2 - non-neg. int
;;         col2 - non-neg. int
;;
;; Returns: none
;;
;; Side effects: modifies bitmap (second handle)
(define (draw_line handle row1 col1 row2 col2)
  (send (first handle) set-pen "black" 1 'solid)
  (send (first handle) draw-line
        (* DRAW-SCALE (+ OFFSET col1)) (* DRAW-SCALE (+ OFFSET row1))
        (* DRAW-SCALE (+ OFFSET col2)) (* DRAW-SCALE (+ OFFSET row2))))

;; draw_wall
;; ----------------
;; Draws a unit-length vertical or horizontal line, with
;; (row col) as the top-left corner of a theoretical unit square.
;;
;;   't
;;   *-
;;'l | | 'r
;;    -
;;    'b
;;
;; where * is (row col)
;;
;; Inputs: handle - handle list
;;         row - non-neg. int
;;         col - non-neg. int
;;         dir - symbol in {'t 'r 'b 'l}
;;
;; Returns: none
;;
;; Side effects: modifies bitmap (second handle)
(define (draw_wall handle row col dir)
  (cond
    [(equal? 't dir) (draw_line handle row col row (+ 1 col))]
    [(equal? 'r dir) (draw_line handle row (+ 1 col) (+ 1 row) (+ 1 col))]
    [(equal? 'b dir) (draw_line handle (+ 1 row) col (+ 1 row) (+ 1 col))]
    [(equal? 'l dir) (draw_line handle row col (+ 1 row) col)]))

;; fill_cell
;; ----------------
;; Fills a cell (with an internal 2-pixel inset) with a given color.
;;
;; Inputs: handle - handle list
;;         row - non-neg. int
;;         col - non-neg. int
;;         color - string (https://docs.racket-lang.org/draw/color-database___.html)
;;
;; Returns: none
;;
;; Side effects: modifies bitmap (second handle)
(define (fill_cell handle row col color)
  (send (first handle) set-brush color 'solid)
  (send (first handle) set-pen color 0 'solid)
  (send (first handle) draw-rectangle
        (+ (* DRAW-SCALE (+ OFFSET col)) 2) (+ (* DRAW-SCALE (+ OFFSET row)) 2)
        (- DRAW-SCALE 4) (- DRAW-SCALE 4)))

;; save_image
;; ----------------
;; Saves an image as a .png as the given filename into current working directory.
;; Overwrites existing file with same name.
;;
;; Inputs: handle - handle list
;;         filename - string
;;
;; Returns: #t if file is saved successfully, otherwise #f
;;
;; Side effects: creates (or modifies) file
(define (save_image handle filename)
  (send (second handle) save-file filename 'png))

;; show_image
;; ----------------
;; Displays given image in the interactions window.
;; May not work 100% well on all platforms?
;;
;; Inputs: handle - handle list
;;
;; Returns: image-snip object
;;
;; Side effects: none
(define (show_image handle)
  (make-object image-snip% (second handle)))

