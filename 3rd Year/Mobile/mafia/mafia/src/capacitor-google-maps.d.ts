/// <reference types="@capacitor/google-maps" />

import 'react';

declare module 'react' {
    namespace JSX {
        interface IntrinsicElements {
            'capacitor-google-map': React.DetailedHTMLProps<
                React.HTMLAttributes<HTMLElement>,
                HTMLElement
            >;
        }
    }
}

