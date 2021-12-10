#lang racket
(require "maze-draw.rkt")
(require "maze-text.rkt")

(define full-maze (append* (build-list 40 (lambda (x) (build-list 30 (lambda (y) (list x y #t #t #t #t))))))) ;; x-coord, y-coord, top, right, bottom, left
;; very simple 4-by-4 maze with all walls, as a list of nodes

(define maze-img (blank_maze 40 30))

;; is this efficient? no.
;;             -   -
;; if we have |i | j|
;;             -   -
;;            |k | l|
;;             -   -
;;
;; then i.bottom = k.top
;;      i.right = j.left
;;      ... and so on
;; perfectly reasonable to modify structure
;; or even draw from a list of edges instead of list of nodes+neighbors
;;
;; my personally preferred racket approach is to have a vector of nodes (each with some important information to model a DJS) and a list of edges
;; racket's issue is that we have no great access to pointers/references without getting messy, so
;; we can't do something as simple as updating pointers, but we can simulate union on DJS... we end up with O(n^2) time, rather than
;; the O(n^2) worst-case and O(n) amortized time we could with the text's DJS structure, but this shouldn't slow us down at all.
;; when applying Kruskal's algorithm, I partition the edges into two blocks: those kept (walls) and those removed (our MST)
;; when done, I construct a new vector of nodes with more pertinent fields for applying BFS or DFS than what I had for a DJS (disjoint set)
;; when drawing the maze, I just draw from the list of edges I kept when applying Kruskal's algorithm, but don't let my
;; first thoughts on approaching a problem necessarily sway yours, if something else seems more natural
(define (draw_maze maze handle)
  (for-each (lambda (x)
              (draw_wall handle (first x) (second x) (and (third x) 't))
              (draw_wall handle (first x) (second x) (and (fourth x) 'r))
              (draw_wall handle (first x) (second x) (and (fifth x) 'b))
              (draw_wall handle (first x) (second x) (and (sixth x) 'l))) maze))


;; draws a maze given a filename
(define (draw_maze_file filename)
  (let ([contents (read_maze_file filename)])
        (let ([img (blank_maze (first (first contents)) (second (first contents)))])
              (draw_maze (transform_nodes (first contents) (second contents)) img) img)))

;(show_image (draw_maze_file "maze.txt")) ;; likely empty, as maze.txt is all as - try changing some characters

;(draw_maze full-maze maze-img)
;full-maze
;(show_image maze-img)
(draw_maze_file "maze3.txt")