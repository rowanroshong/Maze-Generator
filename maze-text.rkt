#lang racket
(provide bool_to_int node_to_text text_to_node read_maze_file write_maze_file index transform_nodes)
;(require "maze-generation.rkt")
(require "maze-draw.rkt")
;; Some helper functions for reading and writing our portable maze-string format.
;; To use, say 0-1 instead of #t and #f, drop the bool_to_int in node_to_text
;;                                       or (not (equal? 0)) int text_to_node


;; bool_to_int
;; ----------------
(define (bool_to_int bool)
  (if bool 1 0))

;; index
;; ----------------
;; Helper for converting 1d to 2d
;; Given index and row/col dim,
;; convert 1d index into 2d, assuming
;; row-major order.
;;
;; Inputs: m - int (row dim)
;;         n - int (col dim)
;;         i - int (1d index)
;; Returns: list of two ints (row and col index)
(define (index m n i)
  (let ([row_idx (floor (/ i n))])
    (list row_idx (- i (* row_idx n)))))

;; node_to_text
;; ----------------
;; Converts a 4-tuple (top?, right?, bottom?, left?) to a character.
;;
;; 97 (a) + 1*left + 2*bottom + 4*right + 8*top
;;
;; Inputs: node - 4-tuple of bools
;;
;; Returns: character in range [a..p]
(define (node_to_text node)
  (integer->char (foldl (lambda (new acc) (+ acc (* (expt 2 new) (bool_to_int (list-ref node (- 3 new)))))) 97 (build-list 4 identity))))

;(node_to_text '(#f #f #f #f))
;(node_to_text '(#t #t #t #t))

;; text_to_node
;; ----------------
;; Converts a character in the range[a..p] to a 4-tuple.
;;
;; Inputs: char - a character (#\a through #\p, inclusive)
;;
;; Returns: 4-tuple of bools
(define (text_to_node char)
  (let ([num (- (char->integer char) 97)])
    (map (lambda (x) (not (equal? 0 (bitwise-and num (expt 2 x))))) (reverse (build-list 4 identity)))))
    ;(list (bitwise-and num 8) (bitwise-and num 4) (bitwise-and num 2) (bitwise-and num 1)) 

;(text_to_node #\p) ; 1 1 1 1
;(text_to_node #\a) ; 0 0 0 0
;(text_to_node #\i) ; 1 0 0 0

;; read_maze_file
;; ----------------
;; Reads in a maze file into a list of characters.
;;
;; The first line of a maze file is two integers: rows cols
;; The subsequent rows*cols lines are characters in the range [a..p].
;;
;; Inputs: filename - string
;;
;; Returns: tuple ((rows cols), list of characters)
(define (read_maze_file filename)
  (let ([lines (file->lines filename)])
    (list (map string->number (string-split (first lines))) (map (lambda (x) (string-ref x 0)) (rest lines)))))

;; transform_nodes
;; ----------------
;; Given list of dimensions and list of characters, 
;;
;; The first line of a maze file is two integers: rows cols
;; The subsequent rows*cols lines are characters in the range [a..p].
;;
;; Inputs: dims - list of 2 ints
;;         contents - list of chars (# of which is (first dims) * (second dims))
;;
;; Returns: list (x index y index wall1 wall2 wall3 wall4)

(define (transform_nodes dims contents)
  (map (lambda (x) (append (index (first dims) (second dims) x) (text_to_node (list-ref contents x)))) (range (apply * dims)))
  )




;(read_maze_file "maze.txt") ;'((4 4) (#\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a))

;; write_maze_file
;; ----------------
;; Writes a maze file following specifications in read_maze_file.
;;
;; Not exactly elegant, but it gets the job done.
;;
;; Inputs: filename - string
;;         maze - list of ((rows cols) (list of characters))
;;
;; Returns: None
(define (write_maze_file filename maze)
  (define outfile (open-output-file filename))
  (display (string-join (map number->string (first maze))) outfile) (display "\n" outfile)
  (for-each (lambda (x) (display (node_to_text (list-tail x 2)) outfile) (display "\n" outfile)) (second maze))
  (close-output-port outfile))


(define (write_maze_file2 filename maze)
  (define outfile (open-output-file filename))
  (display (string-join (map number->string (first maze))) outfile) (display "\n" outfile)
  (for-each (lambda (x) (display (third x) outfile) (display "\n" outfile)) (second maze))
  (close-output-port outfile))

;(write_maze_file "maze2.txt" '((4 4) (#\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a #\a)))
  